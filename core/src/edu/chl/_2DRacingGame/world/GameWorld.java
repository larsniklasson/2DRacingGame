package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.mapobjects.*;
import edu.chl._2DRacingGame.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        gameMap.load(PIXELS_PER_METER);
        insertMap();
    }

    // TODO name
    private void insertMap() {
        insertTrackSections();
        insertImmovables();
        insertCheckpoints();
    }

    private void insertTrackSections() {
        for (TrackSection trackSection : gameMap.getTrackSections()) {
            BodyFactory.createStaticBody(b2World, trackSection.getShape(), true, trackSection);
        }
    }

    private void insertImmovables() {
        for (Immovable immovable : gameMap.getImmovables()) {
            BodyFactory.createStaticBody(b2World, immovable.getShape(), false);
        }
    }

    private void insertCheckpoints() {
        for (Map.Entry<Checkpoint, Shape> entry : gameMap.getCheckpoints().entrySet()) {
            Shape shape = entry.getValue();
            Checkpoint checkpoint = entry.getKey();
            BodyFactory.createStaticBody(b2World, shape, true, checkpoint);
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
