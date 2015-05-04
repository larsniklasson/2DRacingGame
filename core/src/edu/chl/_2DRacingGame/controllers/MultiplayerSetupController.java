package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Daniel Sunnerberg
 */
public class MultiplayerSetupController implements RoomRequestListener, ZoneRequestListener, NotifyListener {

    private static final String API_KEY = "1b1136c934963a62964ccdd973e52b476f3977a743451d54c4f5d427d573a517";
    private static final String SECRET_KEY = "a641f46a9b4ce012d502ae86d235de8aa5445c8fa6d16fd76b9ea0d494ea1327";

    private final String connectionUserName;
    private String roomId = null;

    private final WarpClient warpClient;

    private final MultiplayerSetupListener listener;

    public MultiplayerSetupController(MultiplayerSetupListener listener) {
        this.listener = listener;
        connectionUserName = getUserName();
        warpClient = getWarpInstance();
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
    public void findOpponent() {
        warpClient.connectWithUserName(connectionUserName);
    }

    private String getUserName() {
        return "usr-" + new Date().getTime();
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
        warpClient.subscribeRoom(roomId);
        warpClient.getLiveRoomInfo(roomId);
    }

    /**
     * Called when we've joined a room. Will start the game if we have enough players.
     *
     * @param liveRoomInfoEvent
     */
    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
        if (liveRoomInfoEvent.getJoinedUsers().length == 2) {
            startGame();
        }
    }

    private void createEmptyRoom() {
        Gdx.app.log("MultiplayerSetupController", "Found no room, creating a new one.");
        HashMap<String, Object> data = new HashMap<>();
        data.put("result", "");
        // Create the room. The listener "onCreateRoomDone" will then subscribe to the room
        warpClient.createRoom("quickrace-" + connectionUserName, connectionUserName, 2, data);
    }

    private void disconnect() {
        if (roomId != null && !roomId.isEmpty()) {
            warpClient.unsubscribeRoom(roomId);
            warpClient.leaveRoom(roomId);
            warpClient.deleteRoom(roomId);
        }

        warpClient.removeZoneRequestListener(this);
        warpClient.removeZoneRequestListener(this);
        warpClient.removeNotificationListener(this);
        warpClient.disconnect();
    }

    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        warpClient.joinRoom(roomEvent.getData().getId());
    }

    /**
     * Called when a user joins a room we're subscribed to.
     *
     * @param roomData
     * @param userName
     */
    @Override
    public void onUserJoinedRoom(RoomData roomData, String userName) {
        if (connectionUserName.equals(userName)) {
            return;
        }
        Gdx.app.log("MultiplayerSetupController", "Another user joined the room.");
        startGame();
    }

    private void startGame() {
        Gdx.app.log("MultiplayerSetupController", "Starting the game");
        listener.startMultiplayerGame(warpClient);
    }

    // Empty methods to satisfy required interfaces.

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
    public void onUserChangeRoomProperty(RoomData roomData, String s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap1) {
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
