package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import edu.chl._2DRacingGame.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public class MultiplayerSetupController implements RoomRequestListener, ZoneRequestListener, NotifyListener {

    private static final String API_KEY = "1b1136c934963a62964ccdd973e52b476f3977a743451d54c4f5d427d573a517";
    private static final String SECRET_KEY = "a641f46a9b4ce012d502ae86d235de8aa5445c8fa6d16fd76b9ea0d494ea1327";

    /**
     * The player our client controls.
     */
    private final Player player;
    private List<Player> opponents;
    private String roomId = null;

    private final WarpClient warpClient;
    private final MultiplayerSetupListener listener;

    public MultiplayerSetupController(Player player, MultiplayerSetupListener listener) {
        this.player = player;
        this.listener = listener;
        warpClient = getWarpInstance();
        if (warpClient == null) {
            return;
        }
        warpClient.addConnectionRequestListener(new ConnectionRequestListener() {
            @Override
            public void onConnectDone(ConnectEvent connectEvent) {
                if (connectEvent.getResult() == WarpResponseResultCode.SUCCESS) {
                    Gdx.app.log("MultiplayerSetupController", "Successfully connected to AppWarp-servers.");
                    joinRoom();
                } else {
                    Gdx.app.log("MultiplayerSetupController", "Failed to findOpponent to AppWarp: " + connectEvent.getResult());
                    disconnect();
                    // TODO not available
                }
            }

            @Override
            public void onDisconnectDone(ConnectEvent connectEvent) {
            }

            @Override
            public void onInitUDPDone(byte b) {
            }
        });
        warpClient.addZoneRequestListener(this);
        warpClient.addRoomRequestListener(this);
        warpClient.addNotificationListener(this);
    }

    private void joinRoom() {
        warpClient.initUDP();
        warpClient.joinRoomInRange(1, 1, false);
    }

    private WarpClient getWarpInstance() {

        try {
            WarpClient.initialize(API_KEY, SECRET_KEY);
            return WarpClient.getInstance();
        } catch (Exception e) {
            // getInstance doesn't throw an exception but Exception, hence
            // the crazy catch
            // TODO show multiplayer unavailable
            disconnect();
            e.printStackTrace();
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
                disconnect();
                // TODO
                Gdx.app.log("MultiplayerSetupController", "Failed to find a suitable room. Disconnecting.");
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
        return new ArrayList<>(Arrays.asList(players));
    }

    /**
     * Called when we've joined a room. Will start the game if we have enough players.
     *
     * @param e
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

        } else if (e.getJoinedUsers().length == 2) {
            raceReady();
        }
    }

    private void createEmptyRoom() {
        Gdx.app.log("MultiplayerSetupController", "Found no room, creating a new one.");
        String userName = player.getUserName();

        HashMap<String, Object> data = new HashMap<>();
        data.put("hostUserName", userName);
        String playersJson = new Gson().toJson(new Player[]{player});
        data.put("players", playersJson);

        // Create the room. The listener "onCreateRoomDone" will then subscribe to the room
        warpClient.createRoom("quickrace-" + userName, userName, 2, data);
    }

    private void removeClientListeners() {
        warpClient.removeZoneRequestListener(this);
        warpClient.removeZoneRequestListener(this);
        warpClient.removeNotificationListener(this);
    }

    private void disconnect() {
        if (roomId != null && !roomId.isEmpty()) {
            warpClient.unsubscribeRoom(roomId);
            warpClient.leaveRoom(roomId);
            warpClient.deleteRoom(roomId);
        }

        removeClientListeners();
        warpClient.disconnect();
    }

    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        warpClient.joinRoom(roomEvent.getData().getId());
    }

    private void raceReady() {
        Gdx.app.log("MultiplayerSetupController", "Race is ready");
        removeClientListeners();
        listener.raceReady(warpClient, opponents);
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
    @Override
    public void onUserChangeRoomProperty(RoomData roomData, String sender, HashMap<String, Object> properties, HashMap<String, String> lockedPropertiesTable) {
        Gdx.app.log("MultiplayerSetupController", "Recieved room update with player data.");
        String playersJson = (String) properties.get("players");
        List<Player> players = getPlayersFromJson(playersJson);
        updateOpponents(players);
        if (players.size() == 2) { // TODO flexible race size
            raceReady();
        }
    }

    /**
     * Sets the opponents-field to the players in the room, excluding our player.
     *
     * @param roomPlayers
     */
    private void updateOpponents(List<Player> roomPlayers) {
        List<Player> opponents = new ArrayList<>();
        for (Player opponent : roomPlayers) {
            if (this.player.equals(opponent)) {
                continue;
            }
            opponents.add(opponent);
        }
        this.opponents = opponents;
    }

    // Empty methods to satisfy required interfaces.

    @Override
    public void onUserJoinedRoom(RoomData roomData, String userName) {
    }

    @Override
    public void onRoomCreated(RoomData roomData) {
    }

    @Override
    public void onRoomDestroyed(RoomData roomData) {
    }

    @Override
    public void onUserLeftRoom(RoomData roomData, String s) {
    }

    @Override
    public void onUserLeftLobby(LobbyData lobbyData, String s) {
    }

    @Override
    public void onUserJoinedLobby(LobbyData lobbyData, String s) {
    }

    @Override
    public void onChatReceived(ChatEvent chatEvent) {
    }

    @Override
    public void onPrivateChatReceived(String s, String s1) {
    }

    @Override
    public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {
    }

    @Override
    public void onUpdatePeersReceived(UpdateEvent updateEvent) {
    }

    @Override
    public void onMoveCompleted(MoveEvent moveEvent) {
    }

    @Override
    public void onGameStarted(String s, String s1, String s2) {
    }

    @Override
    public void onGameStopped(String s, String s1) {
    }

    @Override
    public void onUserPaused(String s, boolean b, String s1) {
    }

    @Override
    public void onUserResumed(String s, boolean b, String s1) {
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
