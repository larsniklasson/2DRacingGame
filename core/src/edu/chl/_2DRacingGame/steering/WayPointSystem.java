package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class WayPointSystem extends AISteeringSystem {

    private final Array<Vector2> wayPoints;

    public WayPointSystem(OurVehicle vehicle, Array<Vector2> wayPoints, Difficulty difficulty) {
        super(vehicle);

        setSpeeds(vehicle.getAISpeeds(difficulty));


        this.wayPoints = wayPoints;


        LinePath<Vector2> linePath = new LinePath<Vector2>(wayPoints, false);


        FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(this, linePath, 3, 0);



        setSteeringBehavior(followPath);
    }


}
