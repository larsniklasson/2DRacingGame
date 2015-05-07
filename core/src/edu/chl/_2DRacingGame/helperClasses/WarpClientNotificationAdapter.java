package edu.chl._2DRacingGame.helperClasses;

import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;

import java.util.HashMap;

/**
 * @author Daniel Sunnerberg
 */
public class WarpClientNotificationAdapter implements NotifyListener {

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
    public void onUserJoinedRoom(RoomData roomData, String s) {
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
    public void onUserChangeRoomProperty(RoomData roomData, String sender, HashMap<String, Object> properties, HashMap<String, String> lockedPropertiesTable) {
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
}
