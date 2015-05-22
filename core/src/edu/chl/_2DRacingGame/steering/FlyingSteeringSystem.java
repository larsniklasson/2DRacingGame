package edu.chl._2DRacingGame.steering;



import edu.chl._2DRacingGame.gameObjects.Tire;

/**
 * Class for steering a flying vehicle.
 * Designed specifically for the MagicCarpet class. Instead of turning the wheels and applying forces,
 * torque is applied to each tire (in MagicCarpet's case, the only tire) to rotate the vehicle even though it is standing still.
 * Works best when the vehicle has only one wheel.
 *
 * Gives quite a different feel compared to the TireSteeringSystem
 *@author Lars Niklasson
 */
public class FlyingSteeringSystem extends TireSteeringSystem {

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
        for(Tire tire : getWheelSteerable().getTires()){
            float torque = 0;
            if(keys.contains(Key.Left)){
                torque = 3;
            }

            if (keys.contains(Key.Right)){
                torque = -3;
            }

            tire.getBody().applyAngularImpulse(0.1f * tire.getBody().getInertia() * -tire.getBody().getAngularVelocity(), true);
            tire.getBody().applyTorque(torque, true);
        }

    }
}
