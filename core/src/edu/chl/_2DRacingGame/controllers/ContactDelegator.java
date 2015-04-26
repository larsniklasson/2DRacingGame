package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * @author Daniel Sunnerberg
 */
public interface ContactDelegator {
    void enteredCheckpoint(Tire tire, Checkpoint checkpoint, boolean validEntry);
}
