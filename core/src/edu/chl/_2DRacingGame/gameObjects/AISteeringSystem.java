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
 * Created by Lars Niklasson on 2015-05-16.  Most code from Libgdx's AI-LIB tests. https://github.com/libgdx/gdx-ai/tree/master/tests/src/com/badlogic/gdx/ai/tests
 *
 * file in particular: https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/steer/box2d/Box2dSteeringEntity.java
 */
public abstract class AISteeringSystem extends SteeringSystem<OurVehicle> implements Steerable<Vector2>{

    private float wheelAngle;

    private AISpeedHolder speedHolder;

    float boundingRadius;
    boolean tagged = false;

    /*private float maxLinearSpeed = 10;
    private float maxLinearAcceleration = 2000;
    private float maxAngularSpeed = 1000;
    private float maxAngularAcceleration = 1200;
*/
    boolean independentFacing = false;

    protected SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());



    public AISteeringSystem(OurVehicle vehicle) {
        super(vehicle);


    }



    public boolean isIndependentFacing () {
        return independentFacing;
    }



    @Override
    public Vector2 getPosition() {
        return vehicle.getPosition();
    }

    @Override
    public float getOrientation() {
        return vehicle.getDirection();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return vehicle.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return vehicle.getAngularVelocity();
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
        return speedHolder.getMaxLinearSpeed();
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {

        speedHolder.setMaxLinearSpeed(maxLinearSpeed);
    }

    @Override
    public float getMaxLinearAcceleration() {
        return speedHolder.getMaxLinearAcceleration();
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        speedHolder.setMaxLinearAcceleration(maxLinearAcceleration);
    }

    @Override
    public float getMaxAngularSpeed() {
        return speedHolder.getMaxAngularSpeed();
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        speedHolder.setMaxAngularSpeed(maxAngularSpeed);
    }

    @Override
    public float getMaxAngularAcceleration() {
        return speedHolder.getMaxAngularAcceleration();
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        speedHolder.setMaxAngularAcceleration(maxAngularAcceleration);
    }

    public void setSpeeds(AISpeedHolder speeds){
        this.speedHolder = speeds;
    }


    @Override
    public void update (float deltaTime) {
        //TODO implement GroundMaterial support


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
            vehicle.getBody().applyForceToCenter(force, true);
            for(Tire t : vehicle.getTires()){
                t.getBody().applyForceToCenter(force, true);
            }

            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {

                vehicle.getBody().applyTorque(steeringOutput.angular * deltaTime, true);

                for(Tire t : vehicle.getTires()){
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


                for(Tire t : vehicle.getTires()){
                    t.getBody().setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime);
                    //t.getBody().setTransform(t.getBody().getPosition(), newOrientation);
                }
                vehicle.getBody().setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                //getBody().setTransform(getBody().getPosition(), newOrientation);



                float f = newOrientation - vehicle.getBody().getTransform().getRotation();

                vehicle.place(vehicle.getBody().getPosition(), newOrientation);


                //System.out.println("f = " + f);


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

                //System.out.println("wheelangle: " + wheelAngle);


                for(int i = 0; i < vehicle.getTires().size(); i++){
                    if(vehicle.getIsFrontWheelBooleanList().get(i)){
                        Body b = vehicle.getTires().get(i).getBody();
                        b.setTransform(b.getTransform().getPosition(), b.getTransform().getRotation() + wheelAngle);


                    }
                }


            }
        }

        if (anyAccelerations) {



            // Cap the linear speed
            Vector2 velocity = vehicle.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                vehicle.getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
                for(Tire t : vehicle.getTires()){
                    t.getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));

                }
            }

            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (vehicle.getBody().getAngularVelocity() > maxAngVelocity) {
                vehicle.getBody().setAngularVelocity(maxAngVelocity);
                for(Tire t : vehicle.getTires()){
                    t.getBody().setAngularVelocity(maxAngVelocity);

                }
            }
        }
    }
}
