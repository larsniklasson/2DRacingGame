package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.mapobjects.Immovable;
import edu.chl._2DRacingGame.mapobjects.TrackSection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Map used in a race. Contains the different models which is connected to it, such as checkpoints,
 * spawn points etc.
 *
 * TODO way to nested with Shape objects, they should ideally be created in the world. Most models are related to them, making it hard to overcome smoothly.
 *
 * @author Daniel Sunnerberg
 */
public enum GameMap implements Disposable {

    MAP_1("maps/map1.tmx", "maps/map1.png"),
    MAP_2("maps/map2.tmx", "maps/map2.png");

    private final String mapPath;
    private final String overviewImagePath;
    private TiledMap tiledMap;

    private final List<TrackSection> trackSections = new ArrayList<>();
    private final List<Immovable> immovables = new ArrayList<>();
    private final List<SpawnPoint> spawnPoints = new ArrayList<>();
    private final Map<Checkpoint, Shape> checkpoints = new LinkedHashMap<>();
    private final Array<Vector2> wayPoints = new Array<>();

    /**
     * Creates a new GameMap.
     *
     * @param mapPath path to map source file
     * @param overviewImagePath path to map overview screenshot
     */
    GameMap(String mapPath, String overviewImagePath) {
        this.mapPath = mapPath;
        this.overviewImagePath = overviewImagePath;
    }

    /**
     * @return path to map source file
     */
    public String getPath() {
        return mapPath;
    }

    /**
     * @return path to map overview screenshot
     */
    public String getOverviewImagePath() {
        return overviewImagePath;
    }

    /**
     * @return the map converted to tiled format, this is done by #load(...)-method
     * @see #load(MapLoader)
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * @return all track sections (such as dirt) on the map
     */
    public List<TrackSection> getTrackSections() {
        return trackSections;
    }

    void addTrackSection(TrackSection trackSection) {
        trackSections.add(trackSection);
    }

    /**
     * @return all immovables (such as walls) on the map
     */
    public List<Immovable> getImmovables() {
        return immovables;
    }

    void addImmovable(Immovable immovable) {
        immovables.add(immovable);
    }

    /**
     * @return all spawn points on the map
     */
    public List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    void addSpawnPoint(SpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    /**
     * @return all checkpoints on the map
     */
    public Map<Checkpoint, Shape> getCheckpoints() {
        return checkpoints;
    }

    void addCheckpoint(Checkpoint checkpoint, Shape shape) {
        checkpoints.put(checkpoint, shape);
    }

    /**
     * @return all way points on the map, used by AI
     */
    public Array<Vector2> getWayPoints() {
        return wayPoints;
    }

    void addWayPoint(Vector2 wayPoint) {
        wayPoints.add(wayPoint);
    }

    /**
     * Loads the map's objects using the specified MapLoader.
     * @param mapLoader loader to be used
     */
    public void load(MapLoader mapLoader) {
        tiledMap = mapLoader.loadMap(mapPath);
        mapLoader.insertMapObjects(this);

        if (spawnPoints.isEmpty()) {
            throw new IllegalStateException("Found no spawn-areas on the map.");
        }
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }

}
