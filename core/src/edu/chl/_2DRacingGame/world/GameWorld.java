package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.Dirt;
import edu.chl._2DRacingGame.Ice;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.controllers.CheckpointController;
import edu.chl._2DRacingGame.controllers.ContactController;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.CheckpointFactory;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.models.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Lars Niklasson on 2015-04-21.
 * Revised by Daniel Sunnerberg on 2015-05-10.
 */
public class GameWorld implements Disposable {

    /**
     * Box2D scale factor
     */
    public static float PIXELS_PER_METER = 20f;

    /**
     * Space between each vehicle when spawned on map.
     */
    private static final int VEHICLE_SPAWN_SPACE = 2;

    private final List<Player> players = new ArrayList<>();

    private final GameMode gameMode;
    private final List<Checkpoint> checkpoints = new ArrayList<>();

    private World b2World;
    private TiledMap tiledMap;

    private Vector2 mapSpawnPoint;
    private String mapSpawnDirection;

    private final CheckpointController checkpointController;

    public GameWorld(GameMap gameMap, GameMode gameMode) {
        this.gameMode = gameMode;
        tiledMap = gameMap.load();
        b2World = new World(new Vector2(0, 0), true);

        checkpointController = new CheckpointController(this.gameMode, checkpoints);

        createShapesFromMap();
        if (mapSpawnPoint == null) {
            throw new IllegalStateException("Found no spawn-area on the map.");
        }

        b2World.setContactListener(new ContactController((checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(checkpoint);
            } else {
                checkpointController.invalidPassing(checkpoint);
            }
        }));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addPlayers(List<Player> players) {
        for (Player player : players) {
            addPlayer(player);
        }
    }

    public World getb2World(){
        return b2World;
    }

    public void update(float delta) {
        b2World.step(delta, 3, 3);
        Set<InputManager.PressedKey> keys = InputManager.pollForInput();
        for (Player player : players) {
            if (player.isControlledByClient()) {
                // We should only control our own vehicle ...
                player.getVehicle().update(keys);
            } else {
                // ... just move the opponents texture
                player.getVehicle().updateSprite();
                for(Tire t : player.getVehicle().getTires()){
                    t.updateSprite();
                }


            }
        }
    }

    private void createShapesFromMap(){

        MapLayers ml = tiledMap.getLayers();
        Iterator<MapLayer> it = ml.iterator();

        while(it.hasNext()){
            MapLayer layer = it.next();
            MapObjects mo = layer.getObjects();
            Iterator<MapObject> it2 = mo.iterator();

            while(it2.hasNext()){
                MapObject object = it2.next();
                Shape shape = ShapeFactory.createShape(object, PIXELS_PER_METER);

                if(object.getName().equals("dirt")){
                    new TrackSection(b2World, shape, new Dirt());
                } else if(object.getName().equals("ice")){
                    new TrackSection(b2World, shape, new Ice());
                } else if (object.getName().equals("solid")){
                    new Immovable(b2World, shape);
                } else if (object.getProperties().get("type").equals("checkpoint")) {

                    CheckpointType type = CheckpointType.getTypeFromName(
                        (String) object.getProperties().get("checkpointType")
                    );

                    CheckpointDirection direction = CheckpointDirection.getDirectionFromName(
                            (String) object.getProperties().get("checkpointDirection")
                    );
                    Checkpoint cp = CheckpointFactory.createCheckpoint(b2World, shape, type);
                    cp.addAllowedPassingDirection(direction);
                    checkpoints.add(cp);

                } else if(object.getName().equals("start")){
                    Rectangle r = ((RectangleMapObject) object).getRectangle();
                    mapSpawnPoint = r.getCenter(new Vector2());
                    mapSpawnDirection = (String) object.getProperties().get("type");
                }

            }
        }

    }

    public void spawnPlayers() {
        Vector2 spawnPoint = mapSpawnPoint.cpy();
        for (Player player : players) {
            player.getVehicle().place(spawnPoint, getMapSpawnAngle());

            switch (mapSpawnDirection) {
                case "WEST":
                    spawnPoint = spawnPoint.add(VEHICLE_SPAWN_SPACE, 0);
                    break;
                case "NORTH":
                    spawnPoint = spawnPoint.add(0, -VEHICLE_SPAWN_SPACE);
                    break;
                case "EAST":
                    spawnPoint = spawnPoint.add(-VEHICLE_SPAWN_SPACE, 0);
                    break;
                case "SOUTH":
                    spawnPoint = spawnPoint.add(0, VEHICLE_SPAWN_SPACE);
                    break;
            }
        }

    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void dispose() {
        b2World.dispose();
        tiledMap.dispose();
    }

    public float getMapSpawnAngle() {
        switch(mapSpawnDirection.toUpperCase()){
            case "SOUTH":
                return (float) Math.PI;
            case "WEST":
                return (float) (Math.PI/2);
            case "EAST":
                return (float) (3 * Math.PI/2);
            case "NORTH":
            default:
                return 0;
        }
    }

}
