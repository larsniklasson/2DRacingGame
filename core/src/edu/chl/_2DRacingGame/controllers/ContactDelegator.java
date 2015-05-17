package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * @author Daniel Sunnerberg
 */
interface ContactDelegator {
    void enteredCheckpoint(Checkpoint checkpoint, boolean validEntry);
}
