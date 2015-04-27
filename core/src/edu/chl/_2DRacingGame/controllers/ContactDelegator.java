package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * @author Daniel Sunnerberg
 */
public interface ContactDelegator {
    void enteredCheckpoint(Car car, Checkpoint checkpoint, boolean validEntry);
}
