package edu.chl._2DRacingGame.controllers;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Daniel Sunnerberg
 */
public class MultiplayerSetupController implements RoomRequestListener {

    private static final String API_KEY = "1b1136c934963a62964ccdd973e52b476f3977a743451d54c4f5d427d573a517";
    private static final String SECRET_KEY = "a641f46a9b4ce012d502ae86d235de8aa5445c8fa6d16fd76b9ea0d494ea1327";

    private final WarpClient warpClient;

    public MultiplayerSetupController() {
        warpClient = getWarpInstance();
        warpClient.addConnectionRequestListener(new ConnectionRequestListener() {
            @Override
            public void onConnectDone(ConnectEvent connectEvent) {
                if (connectEvent.getResult() == WarpResponseResultCode.SUCCESS) {
                    joinRoom();
                } else {
                    System.out.println(connectEvent.getResult());
                    // TODO not available
                    System.out.println("An error occured, could not connect.");
                }
            }

            @Override
            public void onDisconnectDone(ConnectEvent connectEvent) {
            }

            @Override
            public void onInitUDPDone(byte b) {
            }
        });
        warpClient.addRoomRequestListener(this);
    }

    private void joinRoom() {
        warpClient.initUDP();
        warpClient.joinRoomInRange(1, 2, false);
    }

    private WarpClient getWarpInstance() {

        try {
            WarpClient.initialize(API_KEY, SECRET_KEY);
            return WarpClient.getInstance();
        } catch (Exception e) {
            // getInstance doesn't throw an exception but Exception, hence
            // the crazy catch
            // TODO show multiplayer unavailable
            e.printStackTrace();
            return null;
        }
    }

    public void connect() {
        System.out.println(getUserName());
        warpClient.connectWithUserName(getUserName());
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
                warpClient.disconnect();
                System.out.println("Failed to join room."); // TODO
                break;
        }
    }

    private void successfullyJoinedRoom(String roomId) {
        System.out.println("Found room!");
        warpClient.subscribeRoom(roomId);
    }

    private void createEmptyRoom() {
        System.out.println("Didn't find a room; creating one instead!");
        HashMap<String, Object> data = new HashMap<>();
        data.put("result", "");
        String userName = getUserName();
        warpClient.createRoom(userName, userName, 2, data);
        warpClient.getLiveRoomInfo(userName);
    }


    // Empty methods to satisfy required interfaces.
    @Override
    public void onSubscribeRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {
    }

    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {
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
