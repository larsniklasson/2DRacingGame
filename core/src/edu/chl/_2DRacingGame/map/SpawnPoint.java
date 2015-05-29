package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a spawn point on the map, along with an angle for that point.
 *
 * @author Daniel Sunnerberg
 */
public class SpawnPoint {

    private final Vector2 position;
    private final float angle;

    /**
     * Creates a new spawn point.
     *
     * @param position position of the spawnpoint
     * @param angle angle of which the spawned object will be facing
     */
    public SpawnPoint(Vector2 position, float angle) {
        this.position = position;
        this.angle = angle;
    }

    /**
     * @return the spawn points position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return the spawn points angle
     */
    public float getAngle() {
        return angle;
    }

}
