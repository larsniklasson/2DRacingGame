package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointController {

    public void invalidPassing(Car car, Checkpoint checkpoint) {
        System.out.println("Invalid checkpoint passing");
    }

    public void validPassing(Car car, Checkpoint checkpoint) {
        System.out.println("Valid checkpoint passing");
    }

}
