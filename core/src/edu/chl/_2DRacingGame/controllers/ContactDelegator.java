package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * @author Daniel Sunnerberg
 */
public interface ContactDelegator {
    public void enteredCheckpoint(Checkpoint checkpoint, boolean validEntry);
}
