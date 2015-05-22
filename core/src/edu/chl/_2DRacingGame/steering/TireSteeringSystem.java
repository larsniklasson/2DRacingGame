package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.helperClasses.Box2DUtils;

import java.util.Set;

/**
 * SteeringSystem for vehicles with tires. Designed for the WheeledVehicle class
 * Is used to control the player's vehicle by changing the direction of the front-wheels and
 * applying forces to the tires.
 *
 *@author Lars Niklasson
 */
public class TireSteeringSystem implements ISteeringSystem{


    private final WheelSteerable ws;


    protected Set<Key> keys;

    private final SteeringInputListener steeringListener;

    /**
     * Creates a steering-system for the specified wheel-steerable with the specified steering-listener, which handles user input.
     * @param wheelSteerable The wheel-steerable the steering-system is created for.
     * @param steeringListener The steering-listener for this steering-system
     */
    public TireSteeringSystem(WheelSteerable wheelSteerable, SteeringInputListener steeringListener) {
        this.ws = wheelSteerable;
        this.steeringListener = steeringListener;
    }

    /**
     * Updates the steering-system's wheel-steerable based on the time elapsed since last update.
     *
     * Gets input from the listener steers/drives the vehicle based on the input.
     * @param delta The time elapsed since last update.
     */
    @Override
    public void update(float delta) {

        keys = steeringListener.getInput();

        turnWheels();
        for(Tire t : ws.getTires()){
            t.updateValues();
            updateDrive(t);
            updateFriction(t);
        }

    }

    private void updateDrive(Tire t) { //TODO make a general method for any body - so it can be used in other SteeringSystems (includes friction methods)



        float desiredSpeed;
        if (keys.contains(Key.Up)) {

            desiredSpeed = t.getCurrentMaxForwardSpeed();
        } else if (keys.contains(Key.Down)) {

            desiredSpeed = t.getCurrentMaxBackwardSpeed();
        } else {
            return;
        }



        Vector2 currentForwardNormal = t.getBody().getWorldVector(new Vector2(0, 1));

        float currentSpeed = Box2DUtils.getForwardVelocity(t.getBody()).dot(currentForwardNormal);


        float force;

        //accelerate up to a certain point. (max speed)

        if (desiredSpeed > currentSpeed) {
            force = t.getDriveForce();
        } else if (desiredSpeed < currentSpeed) {
            force = -t.getDriveForce();
        } else {
            return;
        }


        t.getBody().applyForceToCenter(currentForwardNormal.cpy().scl(force), true);

    }


    private void updateFriction(Tire t) {
        reduceSideWaysVelocity(t);


        applyRoadFriction(t);


    }

    private void applyRoadFriction(Tire t) {
        Vector2 currentForwardNormal = Box2DUtils.getForwardVelocity(t.getBody());

        t.getBody().applyForceToCenter(currentForwardNormal.scl(t.getCurrentBackwardsFriction()), true);
    }

    private void reduceSideWaysVelocity(Tire t) {
        Vector2 impulse = Box2DUtils.getLateralVelocity(t.getBody()).cpy().scl(t.getBody().getMass() * -1);

        //the amount of sideways velocity cancelled cant exceed a certain maximum value - creating the skidding/sliding effect

        if (impulse.len() > t.getCurrentMaxLateralImpulse()) {
            impulse.scl(t.getCurrentMaxLateralImpulse() / impulse.len());
        }

        //cancel out sideways velocity
        t.getBody().applyLinearImpulse(impulse, t.getBody().getWorldCenter(), true);
    }



    protected void turnWheels() {
        if(ws.getFrontJoints().size() == 0)return;




        float lockAngle = MathUtils.degreesToRadians * ws.getMaxTurnAngle();

        float turnRadiansPerSec = ws.getTurnDegreesPerSecond() * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnRadiansPerSec / 60f;
        float desiredAngle = 0;

        if (keys.contains(Key.Left)) {
            desiredAngle = lockAngle;

        } else if (keys.contains(Key.Right)) {
            desiredAngle = -lockAngle;
        }

        float angleNow = ws.getFrontJoints().get(0).getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        for(RevoluteJoint rj : ws.getFrontJoints()){
            rj.setLimits(newAngle, newAngle);
        }

        ws.setCurrentFrontWheelAngle(newAngle);


    }

    /**
     *
     * @return This steering-system's wheel-steerable
     */
    public WheelSteerable getWheelSteerable(){
        return ws;
    }





}
