package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import edu.chl._2DRacingGame.gameModes.RaceListener;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.helperClasses.WarpClientNotificationAdapter;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.world.UpdateListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Sunnerberg
 *
 * TODO users colliding case
 * TODO proper delete/disconnect of room/AppWarp
 * TODO despawn opponent when reached the goal
 */
public class MultiplayerWorldSyncer implements UpdateListener, RaceListener {

    /**
     * Minimum time in ms before next update is sent to opponents.
     */
    private static final int MIN_UPDATE_WAIT = 250;
    /**
     * The time when the last update was sent.
     */
    private long lastSyncTime = 0;

    private final Player clientPlayer;
    private final List<Player> players;

    private final WarpClient warpClient;
    private final List<OpponentListener> opponentListeners = new ArrayList<>();

    public MultiplayerWorldSyncer(WarpClient warpClient, Player clientPlayer, List<Player> players) {
        this.warpClient = warpClient;
        this.clientPlayer = clientPlayer;
        this.players = players;

        warpClient.addNotificationListener(new WarpClientNotificationAdapter() {
            @Override
            public void onUpdatePeersReceived(UpdateEvent updateEvent) {
                recievedUpdate(new String(updateEvent.getUpdate()));
            }
        });
    }

    public void addOpponentListener(OpponentListener listener) {
        opponentListeners.add(listener);
    }

    private Map<String, String> getUpdateProperties(String json) {
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        return new Gson().fromJson(json, mapType);
    }

    private void recievedUpdate(String updateJson) {
        Map<String, String> updateData = getUpdateProperties(updateJson);

        String updateType = updateData.get("updateType");
        if (updateType.equals(MultiplayerUpdateType.LOCATION_UPDATE.name())) {
            processOpponentMoved(updateData);
        } else if (updateType.equals(MultiplayerUpdateType.FINISHED_RACE.name())) {
            processOpponentFinishedRace(updateData);
        } else {
            throw new IllegalStateException("Invalid update type: " + updateType);
        }
    }

    private void processOpponentMoved(Map<String, String> updateData) {
        // We will get these events when we send updates regarding our own clientPlayer;
        // these does obviously not need to be handled again.
        if (updateData.get("senderUserName").equals(clientPlayer.getUserName())) {
            return;
        }

        float x = Float.parseFloat(updateData.get("x"));
        float y = Float.parseFloat(updateData.get("y"));
        float angle = Float.parseFloat(updateData.get("angle"));
        float frontWheelAngle = Float.parseFloat(updateData.get("front_wheel_angle"));
        String senderUserName = updateData.get("senderUserName");

        for (Player opponent : players) {
            if (senderUserName.equals(opponent.getUserName())) {
                moveOpponent(opponent, x, y, angle, frontWheelAngle);
            }
        }
    }

    private void notifyOpponentListeners(Player opponent, Double raceTime) {
        for (OpponentListener listener : opponentListeners) {
            listener.opponentFinished(opponent, raceTime);
        }
    }

    private void processOpponentFinishedRace(Map<String, String> updateData) {
        Gdx.app.log("MultiplayerGameWorld", "A player finished the race.");

        String finishedUserName = updateData.get("senderUserName");
        Double raceTime = Double.valueOf(updateData.get("raceTime"));
        for (Player player : players) {
            if (finishedUserName.equals(player.getUserName())) {
                notifyOpponentListeners(player, raceTime);
                return;
            }
        }
    }

    /**
     * Moves the opponent smoothly to the passed location and adjusts the vehicles angle.
     * Does not perform any path finding to avoid walls etc, should therefore be called often to
     * ensure a smooth path.
     *
     * @param opponent
     * @param x
     * @param y
     * @param angle
     * @param frontWheelAngle
     */
    private void moveOpponent(Player opponent, float x, float y, float angle, float frontWheelAngle) {
        Vehicle opponentVehicle = opponent.getVehicle();
        Vector2 opponentLocation = opponentVehicle.getPosition();
        float oldAngle = opponentVehicle.getDirection();

        if (opponentLocation.equals(new Vector2(x, y)) && angle == oldAngle) {
            return;
        }

        float animationDuration = MIN_UPDATE_WAIT / 1000f;
        Action moveAction = Actions.moveTo(x, y, animationDuration);

        //Hack. We move the sprite instead of the actual tire-body, to avoid weird bugs.
        // hack in action can be seen clearest when playing against a MonsterTruck with debug-mode on.
        if(opponentVehicle instanceof OurVehicle){
            ((OurVehicle)opponentVehicle).setMP_FrontWheelAngle(frontWheelAngle);
        }


        if(oldAngle > 1.5 && angle < -1.5){
            angle += 2* Math.PI;
        }

        if(oldAngle < -1.5 && angle > 1.5){
            angle -= 2*Math.PI;
        }

        Action rotateAction = Actions.rotateTo(angle, animationDuration);

        opponent.getActor().addAction(Actions.parallel(moveAction, rotateAction));
    }

    private long getTimeSinceUpdate() {
        return (System.nanoTime() - lastSyncTime) / 1000000;
    }

    /**
     * Notify our opponents that we finished the race.
     * @param raceTime
     */
    private void notifyFinishedRace(double raceTime) {
        Gdx.app.log("MultiplayerGameWorld", "Notifying opponents that we've finished the race.");

        Map<String, String> updateData = new HashMap<>();
        updateData.put("raceTime", String.valueOf(raceTime));

        sendUpdate(updateData, MultiplayerUpdateType.FINISHED_RACE);
    }

    private void sendLocation(Vector2 vehiclePosition, float angle, float frontWheelAngle) {
        Map<String, String> updateData = new HashMap<>();
        updateData.put("x", "" + vehiclePosition.x);
        updateData.put("y", "" + vehiclePosition.y);
        updateData.put("angle", "" + angle);
        updateData.put("front_wheel_angle", "" + frontWheelAngle);

        sendUpdate(updateData, MultiplayerUpdateType.LOCATION_UPDATE);
    }

    private void sendUpdate(Map<String, String> updateData, MultiplayerUpdateType type) {

        updateData.put("senderUserName", clientPlayer.getUserName());
        updateData.put("updateType", type.name());

        Type typeOfMap = new TypeToken<Map<String, String>>() {}.getType();
        warpClient.sendUpdatePeers(new Gson().toJson(updateData, typeOfMap).getBytes());
    }

    @Override
    public void worldUpdated() {
        // TODO

        Vehicle vehicle = clientPlayer.getVehicle();


        //if vehicle doesn't have front wheels to turn, set it to 0.

        float wheelAngle = 0;
        if(vehicle instanceof OurVehicle){
            wheelAngle = ((OurVehicle)vehicle).getCurrentFrontWheelAngle();
        }


        if (lastSyncTime == 0 || getTimeSinceUpdate() > MIN_UPDATE_WAIT) {
            sendLocation(vehicle.getPosition(), vehicle.getDirection(), wheelAngle);
            lastSyncTime = System.nanoTime();
        }
    }

    /**
     * Called when our vehicle has finished the race.
     *
     * @param raceTime
     * @param message
     */
    @Override
    public void raceFinished(double raceTime, String message) {
        notifyFinishedRace(raceTime);
    }

}
