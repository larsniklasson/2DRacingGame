package edu.chl._2DRacingGame.gameObjects.steering;

import java.util.Set;

/**
 * Interface for getting input regarding the controlling of
 * a vehicle.
 * @author Lars Niklasson
 */

public interface SteeringInputListener{
    /**
     *
     * @return A Set of keys the user pressed.
     */
    Set<Key> getInput();
}