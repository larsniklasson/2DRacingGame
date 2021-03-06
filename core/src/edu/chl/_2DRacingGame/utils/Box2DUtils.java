package edu.chl._2DRacingGame.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Utils class for the Box2D physics engine.
 */
public class Box2DUtils {
    /**
     * Returns the current forward velocity of the specified body.
     * NOTE: "forward" is in this case along the positive direction of the y-axis.
     * (Upwards on downwards depending on coordinate-system.)
     *
     * @param body The body to get the velocity from
     * @return The current forward velocity of the specified body.
     */
    public static Vector2 getForwardVelocity(Body body) {
        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));
        Vector2 copy = new Vector2(currentForwardNormal.x, currentForwardNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }

    /**
     * Returns the current lateral (sideways) velocity of the specified body.
     * NOTE: "sideways" is in this case along the x-axis
     *
     * @param body The body to get the velocity from
     * @return The current lateral velocity of the specified body
     */
    public static Vector2 getLateralVelocity(Body body) {
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
        Vector2 copy = new Vector2(currentRightNormal.x, currentRightNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }



}
