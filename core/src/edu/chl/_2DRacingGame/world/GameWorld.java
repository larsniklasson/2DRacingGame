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
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.mapobjects.*;
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
    public static final float PIXELS_PER_METER = 20f;

    private final List<Player> players = new ArrayList<>();

    // TODO not sure if these are supposed to be here
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    public Array<Vector2> wayPoints = new Array<>();
    private World b2World;

    private TiledMap tiledMap;

    private List<Vector2> mapSpawnPoints = new ArrayList<>();
    private List<Float> mapSpawnAngles = new ArrayList<>();

    private final List<UpdateListener> updateListeners = new ArrayList<>();

    public GameWorld(GameMap gameMap) {
        tiledMap = new TmxMapLoader().load(gameMap.getPath());
        b2World = new World(new Vector2(0, 0), true);

        createShapesFromMap();
        if (mapSpawnPoints.isEmpty()) {
            throw new IllegalStateException("Found no spawn-areas on the map.");
        }
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
        Set<InputManager.PressedKey> keys = InputManager.pollForInput(); // not needed with current steering system
        for (Player player : players) {
            if (player.isControlledByClient()) {
                // We should only control our own vehicle ...
                player.getVehicle().update(delta);
            } else {

                // ... just move the opponents texture
                /*player.getVehicle().updateSprite();
                for(Tire t : player.getVehicle().getTires()){
                    t.updateSprite();
                }*/

                //player.getVehicle().MPspriteUpdate();


            }
        }

        for (UpdateListener listener : updateListeners) {
            listener.worldUpdated();
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

                String objectType = (String) object.getProperties().get("type");
                String objectName = object.getName();


                switch (objectName){
                    case "dirt":
                        new TrackSection(b2World, shape, new Dirt());
                        break;
                    case "ice":
                        new TrackSection(b2World, shape, new Ice());
                        break;
                    case "sand":
                        new TrackSection(b2World, shape, new Sand());
                        break;
                    case "solid":
                        new Immovable(b2World, shape);
                        break;
                    case "path":
                        if(object instanceof EllipseMapObject){
                            Ellipse c = ((EllipseMapObject)object).getEllipse();
                            wayPoints.add(new Vector2(c.x/PIXELS_PER_METER, c.y/PIXELS_PER_METER));
                        } else {
                            throw new IllegalStateException("path must be an Ellipse in Tiled");
                        }

                        break;
                    case "spawn_pos":

                        if(object instanceof PolylineMapObject){
                            Polyline pl = ((PolylineMapObject) object).getPolyline();
                            float[] vertices = pl.getVertices();
                            Vector2 v = new Vector2(vertices[2] - vertices[0], vertices[3] - vertices[1]);

                            mapSpawnPoints.add(new Vector2(vertices[0], vertices[1]));
                            mapSpawnAngles.add((float) (v.angleRad() - Math.PI/2));
                        } else {
                            throw new IllegalStateException("spawn_pos must be a PolyLine in Tiled");
                        }

                        break;
                }

                if(objectType == null){
                    continue;
                }

                switch (objectType){
                    case "checkpoint":

                        CheckpointType type = CheckpointType.getTypeFromName(
                                (String) object.getProperties().get("checkpointType")
                        );

                        List<CheckpointDirection> directions = CheckpointDirection.getDirectionsFromNames(
                                (String) object.getProperties().get("checkpointDirection")
                        );
                        Checkpoint cp = CheckpointFactory.createCheckpoint(b2World, shape, type);
                        for (CheckpointDirection direction : directions) {
                            cp.addAllowedPassingDirection(direction);
                        }
                        checkpoints.add(cp);
                        break;
                }

            }
        }

    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void spawnPlayers() {
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            Vector2 spawnPoint = mapSpawnPoints.get(i);
            float spawnAngle = mapSpawnAngles.get(i);

            p.getVehicle().place(spawnPoint, spawnAngle);
        }
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void dispose() {
        b2World.dispose();
        tiledMap.dispose();
    }

    public void addUpdateListener(UpdateListener listener) {
        updateListeners.add(listener);
    }

    public void removeUpdateListener(UpdateListener listener) {
        updateListeners.remove(listener);
    }
}
