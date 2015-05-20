package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.helperClasses.Box2DUtils;


import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class TireSteeringSystem extends SteeringSystem<OurVehicle>{


    protected Set<Key> keys;

    private final SteeringInputListener steeringListener;

    public TireSteeringSystem(OurVehicle ourVehicle, SteeringInputListener steeringListener) {
        super(ourVehicle);
        this.steeringListener = steeringListener;
    }

    @Override
    public void update(float delta) {

        keys = steeringListener.getInput(); //TODO make some kind of inputprocessor instead, so you dont have to poll every frame. this works fine though

        turnWheels();
        for(Tire t : vehicle.getTires()){
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
        if(vehicle.getFrontJoints().size() == 0)return;




        float lockAngle = MathUtils.degreesToRadians * vehicle.getMaxTurnAngle();

        float turnRadiansPerSec = vehicle.getTurnDegreesPerSecond() * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnRadiansPerSec / 60f;
        float desiredAngle = 0;

        if (keys.contains(Key.Left)) {
            desiredAngle = lockAngle;

        } else if (keys.contains(Key.Right)) {
            desiredAngle = -lockAngle;
        }

        float angleNow = vehicle.getFrontJoints().get(0).getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        for(RevoluteJoint rj : vehicle.getFrontJoints()){
            rj.setLimits(newAngle, newAngle);
        }

        vehicle.setCurrentFrontWheelAngle(newAngle);


    }





}
