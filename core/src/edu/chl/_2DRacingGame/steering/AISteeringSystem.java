package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Tire;

/**
 * SteeringSystem controlled by AI using LibGDX's AI-library. Designed for and requires the OurVehicle class.
 *
 *
 * IMPORTANT NOTE:
 * Most code copied from LibGDX's AI-tests. See https://github.com/libgdx/gdx-ai/tree/master/tests/src/com/badlogic/gdx/ai/tests
 * File in particular: https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/steer/box2d/Box2dSteeringEntity.java
 * Most credit goes to davebaol
 *
 * @author davebaol, Lars Niklasson
 */
public abstract class AISteeringSystem extends SteeringSystem<OurVehicle> implements Steerable<Vector2>{


    private float wheelAngle;

    private AISpeedHolder speedHolder;

    private float boundingRadius;
    private boolean tagged = false;


    private boolean independentFacing = false;

    private SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());


    /**
     * Creates a SteeringSystem for the specified vehicle
     *
     * @param vehicle The vehicle which the steering-system is created for
     */
    public AISteeringSystem(OurVehicle vehicle) {
        super(vehicle);


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


        if (steeringBehavior != null) {
            // Calculate steering acceleration

            steeringBehavior.calculateSteering(steeringOutput);


            // Apply steering acceleration
            applySteering(deltaTime);
        }



    }

    private void applySteering (float deltaTime) {
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
        if (independentFacing) {
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

                }
                vehicle.getBody().setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true


                float f = newOrientation - vehicle.getBody().getTransform().getRotation();

                vehicle.place(vehicle.getBody().getPosition(), newOrientation);


                //calculate and apply the new wheel angle
                float desiredAngle = f * 20;
                if(wheelAngle < desiredAngle){
                    wheelAngle += Math.toRadians(2);
                } else if (wheelAngle > desiredAngle){
                    wheelAngle -= Math.toRadians(2);
                }

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

    /**
     * Sets the steering-behavior (See LibGDX's AI-library) used by the system.
     * @param steeringBehavior The steering-behavior the system will use
     */
    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }
}
