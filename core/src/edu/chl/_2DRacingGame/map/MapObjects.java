package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.map.objects.Immovable;
import edu.chl._2DRacingGame.map.objects.TrackSection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of lists of all map objects.
 *
 * @author Daniel Sunnerberg
 */
public class MapObjects {

    private final List<TrackSection> trackSections = new ArrayList<>();
    private final List<Immovable> immovables = new ArrayList<>();
    private final List<SpawnPoint> spawnPoints = new ArrayList<>();
    private final Map<Checkpoint, Shape> checkpoints = new LinkedHashMap<>();
    private final Array<Vector2> wayPoints = new Array<>();

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

}
