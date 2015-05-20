package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;

/**
 * AI-steering-system where to AI-controlled vehicle is guided along a list of waypoints, positions in the world in a certain order.
 *
 *@author Lars Niklasson
 */
public class WayPointSystem extends AISteeringSystem {

    private final Array<Vector2> wayPoints;

    /**
     * Creates a waypoint-system for the specified vehicle, with the specified waypoints and difficulty
     * @param vehicle The vehicle the waypoint-system is created for
     * @param wayPoints The waypoints the vehicle will be guided along
     * @param difficulty The difficulty-setting for the AI-controlled vehicle
     */
    public WayPointSystem(OurVehicle vehicle, Array<Vector2> wayPoints, Difficulty difficulty) {
        super(vehicle);

        setSpeeds(vehicle.getAISpeeds(difficulty));


        this.wayPoints = wayPoints;


        LinePath<Vector2> linePath = new LinePath<Vector2>(wayPoints, false);


        FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(this, linePath, 3, 0);



        setSteeringBehavior(followPath);
    }


}
