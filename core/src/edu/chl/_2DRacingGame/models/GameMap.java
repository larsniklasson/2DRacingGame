package edu.chl._2DRacingGame.models;

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
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.mapobjects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public enum GameMap implements Disposable {

    MAP_1("maps/map1.tmx", "maps/map1.png"),
    MAP_2("maps/map2.tmx", "maps/map2.png");

    private final String mapPath;
    private final String overviewImagePath;
    private TiledMap tiledMap;

    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final Array<Vector2> wayPoints = new Array<>();
    private final List<Vector2> spawnPoints = new ArrayList<>();
    private final List<Float> spawnAngles = new ArrayList<>();

    private final List<TrackSection> trackSections = new ArrayList<>();

    private float PIXELS_PER_METER = 20f; // TODO remove
    private World world; // TODO remove

    GameMap(String mapPath, String overviewImagePath) {
        this.mapPath = mapPath;
        this.overviewImagePath = overviewImagePath;
    }

    public String getPath() {
        return mapPath;
    }

    public String getOverviewImagePath() {
        return overviewImagePath;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void load(World world) {
        tiledMap = new TmxMapLoader().load(getPath());
        this.world = world;
        createShapesFromMap();
        if (spawnPoints.isEmpty()) { // TODO
            throw new IllegalStateException("Found no spawn-areas on the map.");
        }
    }

    private void processTrackSection(String objectName, Shape shape) {
        GroundMaterial material;
        switch (objectName) {
            case "dirt":
                material = new Dirt();
                break;
            case "ice":
                material = new Ice();
                break;
            case "sand":
                material = new Sand();
                break;
            default:
                return;
        }

        trackSections.add(new TrackSection(shape, material));
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

                processTrackSection(objectName, shape);

                switch (objectName){
                    case "solid":
                        new Immovable(world, shape);
                        break;
                    case "path":
                        if(object instanceof EllipseMapObject){
                            Ellipse c = ((EllipseMapObject)object).getEllipse();
                            wayPoints.add(new Vector2(c.x/ PIXELS_PER_METER, c.y/ PIXELS_PER_METER));
                        } else {
                            throw new IllegalStateException("path must be an Ellipse in Tiled");
                        }

                        break;
                    case "spawn_pos":

                        if(object instanceof PolylineMapObject){
                            Polyline pl = ((PolylineMapObject) object).getPolyline();
                            float[] vertices = pl.getVertices();
                            Vector2 v = new Vector2(vertices[2] - vertices[0], vertices[3] - vertices[1]);

                            spawnPoints.add(new Vector2(vertices[0], vertices[1]));
                            spawnAngles.add((float) (v.angleRad() - Math.PI / 2));
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
                        Checkpoint cp = CheckpointFactory.createCheckpoint(world, shape, type);
                        for (CheckpointDirection direction : directions) {
                            cp.addAllowedPassingDirection(direction);
                        }
                        checkpoints.add(cp);
                        break;
                }

            }
        }

    }

    public List<Vector2> getSpawnPoints() {
        return spawnPoints;
    }

    public List<Float> getSpawnAngles() {
        return spawnAngles;
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public Array<Vector2> getWayPoints() {
        return wayPoints;
    }

    public List<TrackSection> getTrackSections() {
        return trackSections;
    }
}
