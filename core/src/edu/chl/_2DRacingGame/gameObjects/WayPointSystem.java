package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class WayPointSystem extends AISteeringSystem {

    private Array<Vector2> wayPoints;
    private LinePath<Vector2> linePath;
    private FollowPath<Vector2, LinePath.LinePathParam> followPathSB;

    public WayPointSystem(OurVehicle vehicle, Array<Vector2> wayPoints) {
        super(vehicle);


        this.wayPoints = wayPoints;


        linePath = new LinePath<Vector2>(wayPoints, openPath);


        followPathSB = new FollowPath<Vector2, LinePath.LinePathParam>(this, linePath,3); //
        // Setters below are only useful to arrive at the end of an open path

        this.steeringBehavior = followPathSB;
    }

    public Array<Vector2> getWayPoints(){
        return wayPoints;
    }

}
