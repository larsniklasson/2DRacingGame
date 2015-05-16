package edu.chl._2DRacingGame.gameObjects;

import edu.chl._2DRacingGame.helperClasses.InputManager;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class FlyingSteeringSystem extends TireSteeringSystem { //TODO questionable design. forces turnwheels() to not be private

    public FlyingSteeringSystem(OurVehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void turnWheels(){
        for(Tire tire : vehicle.getTires()){
            float torque = 0;
            if(keys.contains(InputManager.PressedKey.Left)){
                torque = 3;
            }

            if (keys.contains(InputManager.PressedKey.Right)){
                torque = -3;
            }

            tire.getBody().applyAngularImpulse(0.1f * tire.getBody().getInertia() * -tire.getBody().getAngularVelocity(), true);
            tire.getBody().applyTorque(torque, true);
        }

    }
}
