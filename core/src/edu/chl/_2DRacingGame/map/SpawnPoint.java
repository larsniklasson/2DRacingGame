package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Daniel Sunnerberg
 */
public class SpawnPoint {

    private final Vector2 position;
    private final float angle;

    public SpawnPoint(Vector2 position, float angle) {
        this.position = position;
        this.angle = angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

}
