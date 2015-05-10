package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.helperClasses.WarpClientNotificationAdapter;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Sunnerberg
 *
 * TODO should probably be a controller
 * TODO map-unique starting positions
 * TODO animate angle
 * TODO stop spinning
 * TODO starting position
 * TODO users colliding case
 * TODO different vehicles
 */
public class MultiplayerGameWorld extends GameWorld {

    /**
     * Minimum time in ms before next update is sent to opponents.
     */
    private static final int MIN_UPDATE_WAIT = 250;

    /**
     * The time when sent the last update.
     */
    private long lastSyncTime = 0;

    /**
     * Player controlled by our client.
     */
    private Player clientPlayer;

    private WarpClient warpClient;

    public MultiplayerGameWorld(GameMap gameMap, GameMode gameMode) {
        super(gameMap, gameMode);
    }

    public void setClientPlayer(Player player) {
        this.clientPlayer = player;
    }

    public void setWarpClient(WarpClient warpClient) {
        this.warpClient = warpClient;

        warpClient.addNotificationListener(new WarpClientNotificationAdapter() {
            @Override
            public void onUpdatePeersReceived(UpdateEvent updateEvent) {
                updateOpponents(new String(updateEvent.getUpdate()));
            }
        });
    }

    private Map<String, String> getUpdateProperties(String json) {
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        return new Gson().fromJson(json, mapType);
    }

    private void updateOpponents(String updateJson) {
        Map<String, String> update = getUpdateProperties(updateJson);

        // We will get these events when we send updates regarding our own clientPlayer;
        // these does obviously not need to be handled again.
        if (update.get("senderUserName").equals(clientPlayer.getUserName())) {
            return;
        }
        Gdx.app.log("MultiplayerGameWorld", "Multiplayer-sync recieved. Updating...");

        float x = Float.parseFloat(update.get("x"));
        float y = Float.parseFloat(update.get("y"));
        float angle = Float.parseFloat(update.get("angle"));
        Vector2 position = new Vector2(x, y);

        String senderUserName = update.get("senderUserName");
        for (Player opponent : getPlayers()) {
            if (senderUserName.equals(opponent.getUserName())) {
                Vehicle opponentVehicle = opponent.getVehicle();
                Vector2 opponentLocation = opponentVehicle.getBody().getTransform().getPosition();
                if (opponentLocation.equals(position)) {
                    return;
                }
                opponentVehicle.resetForces();

                float animationDuration = MIN_UPDATE_WAIT / 1000f;
                Action moveAction = Actions.moveTo(x, y, animationDuration);
                Action updateAngleAction = Actions.rotateTo(angle, animationDuration);
                opponentVehicle.getActor().addAction(moveAction); //Actions.parallel(moveAction, updateAngleAction));
            }
        }
    }

    private long getTimeSinceUpdate() {
        return (System.nanoTime() - lastSyncTime) / 1000000;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (clientPlayer == null) {
            return;
        }

        Body vehicleBody = clientPlayer.getVehicle().getBody();
        if (lastSyncTime == 0 || getTimeSinceUpdate() > MIN_UPDATE_WAIT) {
            sendLocation(vehicleBody.getTransform().getPosition(), vehicleBody.getAngle());
            lastSyncTime = System.nanoTime();
        }
    }

    private void sendLocation(Vector2 vehiclePosition, float angle) {
        Gdx.app.log("MultiplayerGameWorld", "Sending position to other players");

        Map<String, String> update = new HashMap<>();
        update.put("senderUserName", clientPlayer.getUserName());
        update.put("x", "" + vehiclePosition.x);
        update.put("y", "" + vehiclePosition.y);
        update.put("angle", "" + angle);

        Type typeOfMap = new TypeToken<Map<String, String>>() {
        }.getType();
        warpClient.sendUpdatePeers(new Gson().toJson(update, typeOfMap).getBytes());
    }

}
