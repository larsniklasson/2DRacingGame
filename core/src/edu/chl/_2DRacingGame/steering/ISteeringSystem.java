package edu.chl._2DRacingGame.steering;

/**
 * Interface for a steering-system, typically used to control a vehicle.
 *@author Lars Niklasson
 */
public interface ISteeringSystem {
    /**
     * Updates what is supposed to be updated, usually a vehicle, based on the time elapsed since
     * last update.
     * @param delta The time elapsed since last update.
     */
    void update(float delta);

}
