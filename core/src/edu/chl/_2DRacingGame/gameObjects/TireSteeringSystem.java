package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import edu.chl._2DRacingGame.helperClasses.InputManager;

import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class TireSteeringSystem<T extends OurVehicle> extends SteeringSystem{

    Set<InputManager.PressedKey> keys;


    public TireSteeringSystem(T v) {
        super(v);
    }

    @Override
    void update(float delta) {

        keys = InputManager.pollForInput();

        turnWheels();
        for(Tire t : ((T)v).getTires()){
            t.updateValues();
            updateDrive(t);
            updateFriction(t);
        }

    }

    private void updateDrive(Tire t) {



        float desiredSpeed;
        if (keys.contains(InputManager.PressedKey.Up)) {

            desiredSpeed = t.getCurrentMaxForwardSpeed();
        } else if (keys.contains(InputManager.PressedKey.Down)) {

            desiredSpeed = t.getCurrentMaxBackwardSpeed();
        } else {
            return;
        }
        System.out.println("desiredspeed " + desiredSpeed);

        Vector2 currentForwardNormal = t.getBody().getWorldVector(new Vector2(0, 1));

        float currentSpeed = t.getForwardVelocity().dot(currentForwardNormal);


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
        Vector2 currentForwardNormal = t.getForwardVelocity();

        t.getBody().applyForceToCenter(currentForwardNormal.scl(t.getCurrentBackwardsFriction()), true);
    }

    private void reduceSideWaysVelocity(Tire t) {
        Vector2 impulse = t.getLateralVelocity().cpy().scl(t.getBody().getMass() * -1);

        //the amount of sideways velocity cancelled cant exceed a certain maximum value - creating the skidding/sliding effect

        if (impulse.len() > t.getCurrentMaxLateralImpulse()) {
            impulse.scl(t.getCurrentMaxLateralImpulse() / impulse.len());
        }

        //cancel out sideways velocity
        t.getBody().applyLinearImpulse(impulse, t.getBody().getWorldCenter(), true);
    }



    public void turnWheels() {

        T ov = (T) this.v;

        float lockAngle = MathUtils.degreesToRadians * ov.getMaxTurnAngle();

        float turnRadiansPerSec = ov.getTurnPerSecond() * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnRadiansPerSec / 60f;
        float desiredAngle = 0;

        if (keys.contains(InputManager.PressedKey.Left)) {
            desiredAngle = lockAngle;

        } else if (keys.contains(InputManager.PressedKey.Right)) {
            desiredAngle = -lockAngle;
        }

        float angleNow = ov.frontJoints.get(0).getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        for(RevoluteJoint rj : ov.frontJoints){
            rj.setLimits(newAngle, newAngle);
        }


    }





}
