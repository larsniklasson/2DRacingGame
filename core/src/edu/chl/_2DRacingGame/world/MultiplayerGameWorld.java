package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.helperClasses.InputManager;
//import edu.chl._2DRacingGame.helperClasses.MoveAnimator;
import edu.chl._2DRacingGame.helperClasses.WarpClientNotificationAdapter;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Daniel Sunnerberg
 *         TODO should probably be a controller
 */
public class MultiplayerGameWorld extends GameWorld {

    private final List<Player> opponents;

    /**
     * Minimum time in ms before next update is sent to opponents.
     */
    private static final int MIN_UPDATE_WAIT = 1000;

    /**
     * The time when sent the last update.
     */
    private long lastSyncTime = 0;

    private final WarpClient warpClient;

    public MultiplayerGameWorld(Player player, List<Player> opponents, GameMap gameMap, GameMode gameMode, WarpClient warpClient) {
        super(player, gameMap, gameMode);
        this.opponents = opponents;
        this.warpClient = warpClient;

        for (Player opponent : opponents) {
            opponent.setVehicle(new Car(getb2World()));
            // TODO map-unique starting positions
            opponent.getVehicle().place(new Vector2(50f / PIXELS_PER_METER, 50f / PIXELS_PER_METER), 0);
        }

        warpClient.addNotificationListener(new WarpClientNotificationAdapter() {
            @Override
            public void onUpdatePeersReceived(UpdateEvent updateEvent) {
                updateOpponents(new String(updateEvent.getUpdate()));
            }
        });
    }

    private Map<String, String> getUpdateProperties(String json) {
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        return new Gson().fromJson(json, mapType);
    }

    private void updateOpponents(String updateJson) {
        Map<String, String> update = getUpdateProperties(updateJson);

        // We will get these events when we send updates regarding our own player;
        // these does obviously not need to be handled again.
        if (update.get("senderUserName").equals(getPlayer().getUserName())) {
            return;
        }
        Gdx.app.log("MultiplayerGameWorld", "Multiplayer-sync recieved. Updating...");

        float x = Float.parseFloat(update.get("x"));
        float y = Float.parseFloat(update.get("y"));
        float angle = Float.parseFloat(update.get("angle"));
        Vector2 position = new Vector2(x, y);

        String senderUserName = update.get("senderUserName");
        for (Player opponent : opponents) {
            if (senderUserName.equals(opponent.getUserName())) {
                Vector2 opponentLocation = opponent.getVehicle().getBody().getTransform().getPosition();
                if (opponentLocation.equals(position)) {
                    return;
                }
                opponent.getVehicle().place(position, angle);
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        Body vehicleBody = getPlayer().getVehicle().getBody();

        float msSinceLastUpdate = (System.nanoTime() - lastSyncTime) / 1000000;
        if (lastSyncTime == 0 || msSinceLastUpdate > MIN_UPDATE_WAIT) {
            sendLocation(vehicleBody.getTransform().getPosition(), vehicleBody.getAngle());
            lastSyncTime = System.nanoTime();
        }

    }

    private void sendLocation(Vector2 vehiclePosition, float angle) {
        Gdx.app.log("MultiplayerGameWorld", "Sending position to other players");

        Map<String, String> update = new HashMap<>();
        update.put("senderUserName", getPlayer().getUserName());
        update.put("x", "" + vehiclePosition.x);
        update.put("y", "" + vehiclePosition.y);
        update.put("angle", "" + angle);

        Type typeOfMap = new TypeToken<Map<String, String>>() {}.getType();
        warpClient.sendUpdatePeers(new Gson().toJson(update, typeOfMap).getBytes());
    }
}
