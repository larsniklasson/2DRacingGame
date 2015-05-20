package edu.chl._2DRacingGame.steering;


import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Tire;

/**
 * Class for steering a flying vehicle.
 * Designed specifically for the MagicCarpet class. Instead of turning the wheels and applying forces,
 * torque is applied to each tire (in MagicCarpet's case, the only tire) to rotate the vehicle even though it is standing still.
 *
 * Gives quite a different feel to the TireSteeringSystem
 *@author Lars Niklasson
 */
public class FlyingSteeringSystem extends TireSteeringSystem {

    /**
     * Creates a steering-system for the specified vehicle with a specified steering-listener, which handles user input.
     * @param ourVehicle The vehicle the steering-system is created for.
     * @param steeringListener The steering-listener for this steering-system
     */
    public FlyingSteeringSystem(OurVehicle ourVehicle, SteeringInputListener steeringListener) {
        super(ourVehicle, steeringListener);
    }

    //TODO questionable design. forces turnwheels() and keys to not be private


    @Override
    protected void turnWheels(){
        for(Tire tire : vehicle.getTires()){
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
