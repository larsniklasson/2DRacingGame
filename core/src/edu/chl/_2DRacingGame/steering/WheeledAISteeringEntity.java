package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Tire;

/**
 * AI-Steering entity designed for the WheeledVehicle-class.
 *
 * IMPORTANT NOTE:
 * Most code copied from LibGDX's AI-tests. See https://github.com/libgdx/gdx-ai/tree/master/tests/src/com/badlogic/gdx/ai/tests
 * File in particular: https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/steer/box2d/Box2dSteeringEntity.java
 * Most credit goes to davebaol
 *
 * @author davebaol, Lars Niklasson
 */
public class WheeledAISteeringEntity implements AISteeringEntity {

    private final OurVehicle vehicle;

    private float wheelAngle;

    private AISpeedHolder speedHolder;

    private float boundingRadius;
    private boolean tagged = false;


    private boolean independentFacing = false;




    /**
     * Creates a SteeringEntity for the specified vehicle with the specified
     * speeds/accelerations for the steering-entity
     *
     * @param vehicle The vehicle which the SteeringEntity is created for
     * @param speedHolder The speedholder that this entity will get
     */
    public WheeledAISteeringEntity(OurVehicle vehicle, AISpeedHolder speedHolder) {
        this.vehicle = vehicle;
        this.speedHolder = speedHolder;

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

    @Override
    public void setSpeeds(AISpeedHolder speeds){
        this.speedHolder = speeds;
    }




    @Override
    public void applySteering (SteeringAcceleration<Vector2> steeringOutput, float deltaTime) {
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




}
