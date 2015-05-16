package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class AISteeringSystem<T extends OurVehicle> extends SteeringSystem implements Steerable<Vector2>{

    T ov;

    private float wheelAngle;

    private Array<Vector2> wayPoints = new Array<Vector2>();



    LinePath<Vector2> linePath;
    FollowPath<Vector2, LinePath.LinePathParam> followPathSB;

    final boolean openPath = false;


    private float SCALE = GameWorld.PIXELS_PER_METER;

    float boundingRadius;
    boolean tagged = false;

    float maxLinearSpeed = 10;
    float maxLinearAcceleration = 2000;
    float maxAngularSpeed = 1000;
    float maxAngularAcceleration = 1200;

    boolean independentFacing = false;

    protected SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());



    public AISteeringSystem(T v, Array<Vector2> wayPoints) {
        super(v);
        this.ov = v;
        this.wayPoints = wayPoints;


        linePath = new LinePath<Vector2>(wayPoints, openPath);


        followPathSB = new FollowPath<Vector2, LinePath.LinePathParam>(this, linePath,3); //
        // Setters below are only useful to arrive at the end of an open path

        this.steeringBehavior = followPathSB;
    }

    public Array<Vector2> getWayPoints(){
        return wayPoints;
    }

    public boolean isIndependentFacing () {
        return independentFacing;
    }

    public void addWayPoint(Vector2 point){
        wayPoints.add(point);
    }







    @Override
    public Vector2 getPosition() {
        return v.getPosition();
    }

    @Override
    public float getOrientation() {
        return v.getDirection();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return v.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return v.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {


        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearSpeed = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public void update (float deltaTime) {



        if (steeringBehavior != null) {
            // Calculate steering acceleration

            steeringBehavior.calculateSteering(steeringOutput);

			/*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 *
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(steeringOutput, deltaTime);
        }



    }

    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;



        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {

            Vector2 force = steeringOutput.linear.scl(deltaTime);
            ov.getBody().applyForceToCenter(force, true);
            for(Tire t : ov.getTires()){
                t.getBody().applyForceToCenter(force, true);
            }

            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {

                ov.getBody().applyTorque(steeringOutput.angular * deltaTime, true);

                for(Tire t : ov.getTires()){
                    t.getBody().applyTorque(steeringOutput.angular * deltaTime, true);
                }
                anyAccelerations = true;
            }
        }
        else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();

            if (!linVel.isZero()) {

                float newOrientation = vectorToAngle(linVel);


                for(Tire t : ov.getTires()){
                    t.getBody().setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime);
                    //t.getBody().setTransform(t.getBody().getPosition(), newOrientation);
                }
                ov.getBody().setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                //getBody().setTransform(getBody().getPosition(), newOrientation);



                float f = newOrientation - ov.getBody().getTransform().getRotation();

                ov.place(ov.getBody().getPosition(), newOrientation);


                System.out.println("f = " + f);


                /*if(f < -0.04){
                    wheelAngle += (float) Math.toRadians(-50);
                } else if(f > 0.04){
                    wheelAngle = (float) Math.toRadians(50);
                } else if(f > -0.005 && f < 0.005){
                    wheelAngle = 0;
                }*/

                /*if(f < 0.0){
                    wheelAngle = (float) Math.max(Math.toRadians(-50), wheelAngle - Math.toRadians(2));
                } else if(f > 0.0){
                    wheelAngle = (float) Math.min(Math.toRadians(50), wheelAngle + Math.toRadians(2));
                }*/

                float desiredAngle = f * 20;       //WORKS SURPRISINGLY WELL
                if(wheelAngle < desiredAngle){
                    wheelAngle += Math.toRadians(2);
                } else if (wheelAngle > desiredAngle){
                    wheelAngle -= Math.toRadians(2);
                }

                System.out.println("wheelangle: " + wheelAngle);

                Body b = ov.getTires().get(0).getBody();
                b.setTransform(b.getTransform().getPosition(), b.getTransform().getRotation() + wheelAngle);
                b = ov.getTires().get(1).getBody();
                b.setTransform(b.getTransform().getPosition(), b.getTransform().getRotation() + wheelAngle);

            }
        }

        if (anyAccelerations) {



            // Cap the linear speed
            Vector2 velocity = ov.getBody().getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                ov.getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
                for(Tire t : ov.getTires()){
                    t.getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));

                }
            }

            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (ov.getBody().getAngularVelocity() > maxAngVelocity) {
                ov.getBody().setAngularVelocity(maxAngVelocity);
                for(Tire t : ov.getTires()){
                    t.getBody().setAngularVelocity(maxAngVelocity);

                }
            }
        }
    }
}
