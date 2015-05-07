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
import edu.chl._2DRacingGame.helperClasses.WarpClientNotificationAdapter;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Sunnerberg
 *         TODO should probably be a controller
 */
public class MultiplayerGameWorld extends GameWorld {

    private final List<Player> opponents;
    private Vector2 lastSyncedLocation;

    private final WarpClient warpClient;

    public MultiplayerGameWorld(Player player, List<Player> opponents, GameMap gameMap, GameMode gameMode, WarpClient warpClient) {
        super(player, gameMap, gameMode);
        this.opponents = opponents;
        this.warpClient = warpClient;

        for (Player opponent : opponents) {
            opponent.setVehicle(new Car(getb2World()));
            // TODO map-unique starting positions
            opponent.getVehicle().moveTo(new Vector2(50f / PIXELS_PER_METER, 50f / PIXELS_PER_METER), 0);
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
        Gdx.app.log("MultiplayerGameWorld", "Multiplayer-sync recieved. Updating...");
        Map<String, String> update = getUpdateProperties(updateJson);

        float x = Float.parseFloat(update.get("x"));
        float y = Float.parseFloat(update.get("y"));
        float angle = Float.parseFloat(update.get("angle"));
        Vector2 position = new Vector2(x, y);

        String senderUserName = update.get("senderUserName");
        for (Player opponent: opponents) {
            if (senderUserName.equals(opponent.getUserName())) {
                opponent.getVehicle().moveTo(position, angle);
                return;
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        Body vehicleBody = getPlayer().getVehicle().getBody();
        if (lastSyncedLocation == null || ! lastSyncedLocation.equals(vehicleBody.getPosition())) {
            sendLocation(vehicleBody.getPosition(), vehicleBody.getAngle());
            lastSyncedLocation = vehicleBody.getPosition().cpy();
        }

    }

    private void sendLocation(Vector2 vehiclePosition, float angle) {
        Gdx.app.log("MultiplayerGameWorld", "Sending position to other players");

        Map<String, String> update = new HashMap<>();
        update.put("senderUserName", getPlayer().getUserName());
        update.put("x", "" + vehiclePosition.x);
        update.put("y", "" + vehiclePosition.y);
        update.put("angle", "" + angle);
        Type typeOfMap = new TypeToken<Map<String, String>>(){}.getType();
        warpClient.sendUpdatePeers(new Gson().toJson(update, typeOfMap).getBytes());
    }
}
