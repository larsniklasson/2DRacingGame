package edu.chl._2DRacingGame.map;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.mapobjects.*;

import java.util.*;

/**
 * TODO way to nested with Shape objects. They are very comfortable, though.
 * TODO Shapes should ideally be created in World.
 * TODO extract map loading to a separate class
 *
 * @author Daniel Sunnerberg
 */
public enum GameMap implements Disposable {

    MAP_1("maps/map1.tmx", "maps/map1.png"),
    MAP_2("maps/map2.tmx", "maps/map2.png");

    private final String mapPath;
    private final String overviewImagePath;
    private TiledMap tiledMap;

    private final Map<Checkpoint, Shape> checkpoints = new LinkedHashMap<>();
    private final Array<Vector2> wayPoints = new Array<>();
    private final List<Vector2> spawnPoints = new ArrayList<>();
    private final List<Float> spawnAngles = new ArrayList<>();

    private final List<TrackSection> trackSections = new ArrayList<>();
    private List<Immovable> immovables = new ArrayList<>();

    private float scaleFactor;

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

    public void load(float scaleFactor) {
        this.scaleFactor = scaleFactor;
        tiledMap = new TmxMapLoader().load(getPath());
        createShapesFromMap();

        if (spawnPoints.isEmpty()) {
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

    private void processImmovable(String objectName, Shape shape) {
        if (objectName.equals("solid")) {
            immovables.add(new Immovable(shape));
        }
    }

    private void processPath(String objectName, MapObject object, Shape shape) {
        if (!objectName.equals("path")) {
            return;
        }

        if (!(object instanceof EllipseMapObject)) {
            throw new IllegalStateException("Path must be an Ellipse in Tiled");
        }

        Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
        wayPoints.add(new Vector2(ellipse.x / scaleFactor, ellipse.y / scaleFactor));
    }

    private void processSpawnPosition(String objectName, MapObject object, Shape shape) {
        if (!objectName.equals("spawn_pos")) {
            return;
        }

        if (!(object instanceof PolylineMapObject)) {
            throw new IllegalStateException("spawn_pos must be a PolyLine in Tiled");
        }

        Polyline pl = ((PolylineMapObject) object).getPolyline();
        float[] vertices = pl.getVertices();
        Vector2 v = new Vector2(vertices[2] - vertices[0], vertices[3] - vertices[1]);

        spawnPoints.add(new Vector2(vertices[0], vertices[1]));
        spawnAngles.add((float) (v.angleRad() - Math.PI / 2));
    }

    private void processCheckpoint(String objectType, MapObject object, Shape shape) {
        if (objectType == null || ! objectType.equals("checkpoint")) {
            return;
        }

        CheckpointType type = CheckpointType.getTypeFromName(
                (String) object.getProperties().get("checkpointType")
        );

        List<CheckpointDirection> directions = CheckpointDirection.getDirectionsFromNames(
                (String) object.getProperties().get("checkpointDirection")
        );

        Checkpoint checkpoint = new Checkpoint(type);
        for (CheckpointDirection direction : directions) {
            checkpoint.addAllowedPassingDirection(direction);
        }
        checkpoints.put(checkpoint, shape);
    }

    private void createShapesFromMap() {

        // TODO place in another class

        MapLayers ml = tiledMap.getLayers();
        Iterator<MapLayer> it = ml.iterator();

        while (it.hasNext()) {
            MapLayer layer = it.next();
            MapObjects mo = layer.getObjects();
            Iterator<MapObject> it2 = mo.iterator();

            while (it2.hasNext()) {
                MapObject object = it2.next();
                Shape shape = ShapeFactory.createShape(object, scaleFactor);

                String objectType = (String) object.getProperties().get("type");
                String objectName = object.getName();

                processTrackSection(objectName, shape);
                processImmovable(objectName, shape);
                processPath(objectName, object, shape);
                processSpawnPosition(objectName, object, shape);
                processCheckpoint(objectType, object, shape);

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

    public Map<Checkpoint, Shape> getCheckpoints() {
        return checkpoints;
    }

    public Array<Vector2> getWayPoints() {
        return wayPoints;
    }

    public List<TrackSection> getTrackSections() {
        return trackSections;
    }

    public List<Immovable> getImmovables() {
        return immovables;
    }
}
