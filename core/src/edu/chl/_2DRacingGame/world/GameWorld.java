package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.mapobjects.*;
import edu.chl._2DRacingGame.models.CheckpointFactory;
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.models.*;


import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Holds all the game-objects and other things related to the world.
 * Updates the ones the should be updated each frame.
 *
 * Created by Lars Niklasson on 2015-04-21.
 * Revised by Daniel Sunnerberg on 2015-05-10.
 */
public class GameWorld implements Disposable {

    /**
     * Box2D scale factor
     */
    public static final float PIXELS_PER_METER = 20f;

    private final List<Player> players = new ArrayList<>();
    private final List<UpdateListener> updateListeners = new ArrayList<>();
    private final World b2World;
    private final GameMap gameMap;

    /**
     * Creates a world from the specified map
     * @param gameMap the map this world will use.
     */
    public GameWorld(GameMap gameMap) {
        this.gameMap = gameMap;
        b2World = new World(new Vector2(0, 0), true);
        gameMap.load(b2World);
        insertMap();
    }

    // TODO name
    private void insertMap() {
        insertTrackSections();
    }

    private void insertTrackSections() {
        for (TrackSection trackSection : gameMap.getTrackSections()) {

            // TODO extract
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = b2World.createBody(bodyDef);
            body.setUserData(trackSection);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = trackSection.getShape();
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
        }
    }

    /**
     * Adds a player to this world.
     * @param player The player to be added.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Adds a list of players to this world.
     * @param players The list of players to be added.
     */
    public void addPlayers(List<Player> players) {
        for (Player player : players) {
            addPlayer(player);
        }
    }

    /**
     *
     * @return The Box2D-world the GameWorld uses to simulate the physics.
     */
    public World getb2World(){
        return b2World;
    }

    /**
     * Update the physics-engine and the vehicles of each player, based
     * on the time elapsed since last update.
     *
     * @param delta Time elapsed since last update.
     */
    public void update(float delta) {
        b2World.step(delta, 3, 3);

        for (Player player : players) {
            if (player.isControlledLocally()) {
                // We should only control our own vehicle ...

                player.getVehicle().update(delta);
            }
        }

        for (UpdateListener listener : updateListeners) {
            listener.worldUpdated();
        }
    }


    /**
     * Spawns each player on the right spawn-location.
     */
    public void spawnPlayers() {
        List<Vector2> mapSpawnPoints = gameMap.getSpawnPoints();
        List<Float> mapSpawnAngles = gameMap.getSpawnAngles();

        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            Vector2 spawnPoint = mapSpawnPoints.get(i);
            float spawnAngle = mapSpawnAngles.get(i);

            p.getVehicle().place(spawnPoint, spawnAngle);
        }
    }


    /**
     *
     * @return A list of all the players in this game-world.
     */
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void dispose() {
        b2World.dispose();
        gameMap.dispose();
    }


    public void addUpdateListener(UpdateListener listener) {
        updateListeners.add(listener);
    }

    public void removeUpdateListener(UpdateListener listener) {
        updateListeners.remove(listener);
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
