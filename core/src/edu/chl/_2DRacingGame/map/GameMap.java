package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
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

    // TODO sort methods
    void addTrackSection(TrackSection trackSection) {
        trackSections.add(trackSection);
    }

    public void load(MapLoader mapLoader) {
        tiledMap = mapLoader.loadMap(mapPath);
        mapLoader.insertMapObjects(this);

        if (spawnPoints.isEmpty()) {
            throw new IllegalStateException("Found no spawn-areas on the map.");
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

    void addImmovable(Immovable immovable) {
        immovables.add(immovable);
    }

    void addWayPoint(Vector2 wayPoint) {
        wayPoints.add(wayPoint);
    }

    void addSpawnPoint(Vector2 spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    void addSpawnAngle(float spawnAngle) {
        spawnAngles.add(spawnAngle);
    }

    void addCheckpoint(Checkpoint checkpoint, Shape shape) {
        checkpoints.put(checkpoint, shape);
    }
}
