package edu.chl._2DRacingGame.steering;



import edu.chl._2DRacingGame.gameObjects.Wheel;

/**
 * Class for steering a flying vehicle.
 * Designed specifically for the MagicCarpet class. Instead of turning the wheels and applying forces,
 * torque is applied to each wheel (in MagicCarpet's case, the only wheel) to rotate the vehicle even though it is standing still.
 * Works best when the vehicle has only one wheel.
 *
 * Gives quite a different feel compared to the WheelSteeringSystem
 *@author Lars Niklasson
 */
public class FlyingSteeringSystem extends WheelSteeringSystem {

    /**
     * Creates a steering-system for the specified wheelsteerable with a specified steering-listener, which handles user input.
     * @param wheelSteerable The wheelsteerable the steering-system is created for.
     * @param steeringListener The steering-listener for this steering-system
     */
    public FlyingSteeringSystem(WheelSteerable wheelSteerable, SteeringInputListener steeringListener) {
        super(wheelSteerable, steeringListener);
    }


    @Override
    protected void turnWheels(){
        for(Wheel wheel : getWheelSteerable().getWheels()){
            float torque = 0;
            if(keys.contains(Key.Left)){
                torque = 3;
            }

            if (keys.contains(Key.Right)){
                torque = -3;
            }

            wheel.getBody().applyAngularImpulse(0.1f * wheel.getBody().getInertia() * -wheel.getBody().getAngularVelocity(), true);
            wheel.getBody().applyTorque(torque, true);
        }

    }
}
