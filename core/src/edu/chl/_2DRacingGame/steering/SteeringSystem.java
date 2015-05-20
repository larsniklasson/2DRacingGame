package edu.chl._2DRacingGame.steering;

import edu.chl._2DRacingGame.gameObjects.Vehicle;

/**
 * System used to control a vehicle, update position, velocity, etc.
 * NOTE: cyclic dependency with Vehicle.
 *
 *@author Lars Niklasson
 */
public abstract class SteeringSystem<T extends Vehicle>{
    protected T vehicle;

    /**
     * Creates a SteeringSystem for the specified vehicle
     *
     * @param vehicle The vehicle which the steering-system is created for
     */
    public SteeringSystem(T vehicle){
        this.vehicle = vehicle;
    }

    /**
     * Updates the steering-system's vehicle based on the time elapsed since last update.
     *
     * @param delta The time elapsed since last update.
     */
    public abstract void update(float delta);

}
