package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class Box2DUtils {

    public static Vector2 getForwardVelocity(Body body) {
        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));
        Vector2 copy = new Vector2(currentForwardNormal.x, currentForwardNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }

    public static Vector2 getLateralVelocity(Body body) {
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
        Vector2 copy = new Vector2(currentRightNormal.x, currentRightNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }


}
