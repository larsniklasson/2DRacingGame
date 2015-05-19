package edu.chl._2DRacingGame.steering;


import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Tire;

/**
 * Created by Lars Niklasson on 2015-05-16.
 */
public class FlyingSteeringSystem extends TireSteeringSystem {
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
