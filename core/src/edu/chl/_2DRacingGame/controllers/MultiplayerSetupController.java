package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import edu.chl._2DRacingGame.helperClasses.WarpClientNotificationAdapter;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;

import java.util.*;

/**
 * Helps find opponents to a multiplayer race. Players are paired on a first-come basis, with the exception
 * that both must have selected the same map.
 *
 * TODO name
 *
 * @author Daniel Sunnerberg
 */
class MultiplayerSetupController implements RoomRequestListener, ZoneRequestListener, ConnectionRequestListener {

    private static final String API_KEY = "1b1136c934963a62964ccdd973e52b476f3977a743451d54c4f5d427d573a517";
    private static final String SECRET_KEY = "a641f46a9b4ce012d502ae86d235de8aa5445c8fa6d16fd76b9ea0d494ea1327";

    /**
     * We have, for now, decided that a multiplayer race will only have two players.
     */
    private static final int RACE_SIZE = 2;

    /**
     * Player controlled by our client.
     */
    private final Player player;

    /**
     * Players in the room; including our player if we've successfully joined the room.
     */
    private List<Player> roomPlayers;

    /**
     * We want to find other players who want to play the same map
     */
    private final GameMap map;

    private String roomId = null;

    private final WarpClient warpClient;

    private final MultiplayerSetupListener listener;
    private WarpClientNotificationAdapter notificationAdapter;

    /**
     * Connects to AppWarp-servers and prepares the instance to be able to find opponents.
     *
     * @param player Our clients player
     * @param map Map to search opponents for
     * @param listener Listener to be notified when related events occur
     */
    public MultiplayerSetupController(Player player, GameMap map, MultiplayerSetupListener listener) {
        this.player = player;
        this.map = map;
        this.listener = listener;

        warpClient = getWarpInstance();
        if (warpClient == null) {
            return;
        }

        warpClient.addConnectionRequestListener(this);
        warpClient.addZoneRequestListener(this);
        warpClient.addRoomRequestListener(this);
        notificationAdapter = new WarpClientNotificationAdapter() {
            @Override
            public void onUserChangeRoomProperty(RoomData roomData, String sender, HashMap<String, Object> properties, HashMap<String, String> lockedPropertiesTable) {
                MultiplayerSetupController.this.onUserChangeRoomProperty(roomData, sender, properties, lockedPropertiesTable);
            }
        };
        warpClient.addNotificationListener(notificationAdapter);
    }

    private WarpClient getWarpInstance() {
        try {
            WarpClient.initialize(API_KEY, SECRET_KEY);
            return WarpClient.getInstance();
        } catch (Exception e) {
            // getInstance doesn't throw an exception but Exception, hence
            // the crazy catch
            listener.connectionError("Failed to get AppWarp connection. Stacktrace: " + Throwables.getStackTraceAsString(e));
            disconnect();
            return null;
        }
    }

    /**
     * Finds a room with a suitable number of opponents.
     * If none are found, a room will be created while waiting for an opponent.
     */
    public void findRace() {
        warpClient.connectWithUserName(player.getUserName());
    }

    /**
     * Called when we have attempted to connect to AppWarp-servers.
     *
     * @param connectEvent Event containing result codes etc.
     */
    @Override
    public void onConnectDone(ConnectEvent connectEvent) {
        if (connectEvent.getResult() == WarpResponseResultCode.SUCCESS) {
            Gdx.app.log("MultiplayerSetupController", "Successfully connected to AppWarp-servers.");
            joinRoom();
        } else {
            listener.connectionError("Failed to connect to AppWarp: " + connectEvent.getResult());
            disconnect();
        }
    }

    private void joinRoom() {
        warpClient.initUDP();

        HashMap<String, Object> roomProperties = new HashMap<>();
        roomProperties.put("map", map.name());
        warpClient.joinRoomWithProperties(roomProperties);
    }

    @Override
    public void onJoinRoomDone(RoomEvent roomEvent) {
        switch (roomEvent.getResult()) {
            case WarpResponseResultCode.SUCCESS:
                successfullyJoinedRoom(roomEvent.getData().getId());
                break;
            case WarpResponseResultCode.RESOURCE_NOT_FOUND:
                createEmptyRoom();
                break;
            default:
                listener.connectionError("Failed to find a suitable room. Response code: " + roomEvent.getResult());
                disconnect();
                break;
        }
    }

    private void successfullyJoinedRoom(String roomId) {
        Gdx.app.log("MultiplayerSetupController", "Joined room. Subscribing to it.");
        this.roomId = roomId;
        warpClient.subscribeRoom(roomId);
        warpClient.getLiveRoomInfo(roomId);
    }

    private List<Player> getPlayersFromJson(String playersJson) {
        Player[] players = new Gson().fromJson(playersJson, Player[].class);
        List<Player> playersList = new ArrayList<>();
        for (Player roomPlayer : players) {
            if (player.equals(roomPlayer)) {
                playersList.add(player);
            } else {
                roomPlayer.setControlledLocally(false);
                playersList.add(roomPlayer);
            }
        }
        return playersList;
    }

    /**
     * Called when we've joined a room. Will start the game if we have enough players.
     *
     * @param e information about the joined room
     */
    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent e) {
        HashMap<String, Object> roomProperties = e.getProperties();
        if (!roomProperties.get("hostUserName").equals(player.getUserName())) {
            Gdx.app.log("MultiplayerSetupController", "Updating room data to include our player.");

            // We joined a room which we didn't create, add our Player-information to it.
            String playersJson = (String) roomProperties.get("players");
            List<Player> playersList = getPlayersFromJson(playersJson);
            playersList.add(player);

            playersJson = new Gson().toJson(playersList);
            roomProperties.put("players", playersJson);
            warpClient.updateRoomProperties(roomId, roomProperties, null);

        } else if (e.getJoinedUsers().length == RACE_SIZE) {
            raceReady();
        }
    }

    private void createEmptyRoom() {
        Gdx.app.log("MultiplayerSetupController", "Found no room, creating a new one.");
        String userName = player.getUserName();

        HashMap<String, Object> data = new HashMap<>();
        data.put("hostUserName", userName);
        data.put("map", map.name());

        // We want the room to have a property with the players (serialized) in the room.
        // This functionality isn't automatically provided, hence the serialization below
        String playersJson = new Gson().toJson(new Player[]{player});
        data.put("players", playersJson);

        String roomName = "quickrace-" + userName;
        // Create the room. The listener "onCreateRoomDone" will then subscribe to the room
        warpClient.createRoom(roomName, userName, RACE_SIZE, data);
    }

    private void removeClientListeners() {
        warpClient.removeConnectionRequestListener(this);
        warpClient.removeZoneRequestListener(this);
        warpClient.removeZoneRequestListener(this);
        warpClient.removeNotificationListener(notificationAdapter);
    }

    private void disconnect() {
        if (roomId != null && !roomId.isEmpty()) {
            warpClient.unsubscribeRoom(roomId);
            warpClient.leaveRoom(roomId);
        }

        removeClientListeners();
        warpClient.disconnect();
    }

    /**
     * Called when we have created a room.
     *
     * @param roomEvent information about created room
     */
    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        warpClient.joinRoom(roomEvent.getData().getId());
    }

    private void raceReady() {
        Gdx.app.log("MultiplayerSetupController", "Race is ready");
        removeClientListeners();
        listener.raceReady(roomId, warpClient, roomPlayers);
    }

    /**
     * Called when we/another user updates the room data with their information.
     * If we have enough players, start the game.
     *
     * @param roomData
     * @param sender
     * @param properties
     * @param lockedPropertiesTable
     */
    private void onUserChangeRoomProperty(RoomData roomData, String sender, HashMap<String, Object> properties, HashMap<String, String> lockedPropertiesTable) {
        Gdx.app.log("MultiplayerSetupController", "Recieved room update with player data.");
        String playersJson = (String) properties.get("players");
        roomPlayers = getPlayersFromJson(playersJson);
        if (roomPlayers.size() == RACE_SIZE) {
            raceReady();
        }
    }

    // Empty methods to satisfy required interfaces.

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {
    }

    @Override
    public void onInitUDPDone(byte b) {
    }

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {
    }

    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {
    }

    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {
    }

    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {
    }

    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {
    }

    @Override
    public void onSubscribeRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {
    }

    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent liveRoomInfoEvent) {
    }

    @Override
    public void onLockPropertiesDone(byte b) {
    }

    @Override
    public void onUnlockPropertiesDone(byte b) {
    }
}
