package edu.chl._2DRacingGame.vehicle.steering;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * AI-steering-system where the AI-controlled entity is guided along a list of waypoints, coordinates in the 2D-world.
 *
 *@author Lars Niklasson
 */
public class WayPointSystem extends AISteeringSystem {



    /**
     * Creates a waypoint-system for the specified AISteeringEntity, with the specified waypoints
     * @param steeringEntity The entity the waypoint-system is created for
     * @param wayPoints The waypoints the vehicle will be guided along
     */
    public WayPointSystem(AISteeringEntity steeringEntity, Array<Vector2> wayPoints) {
        super(steeringEntity);


        LinePath<Vector2> linePath = new LinePath<>(wayPoints, false);

        FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(steeringEntity, linePath, 3, 0);

        setSteeringBehavior(followPath);
    }


}
