package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.models.Checkpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointController {

    /**
     * A list of the checkpoints in the map, in the order they are expected to be occured.
     */
    private final List<Checkpoint> checkpoints;
    private final Map<Checkpoint, Car> checkpointHistory = new HashMap<>(); // TODO should be Vehicle

    public CheckpointController(List<Checkpoint> mapCheckpoints) {
        this.checkpoints = mapCheckpoints;
    }

    public void invalidPassing(Car car, Checkpoint checkpoint) {
        System.out.println("Invalid checkpoint passing.");
    }

    public void validPassing(Car car, Checkpoint checkpoint) {
        if (checkpointHistory.get(checkpoint) != null) {
            System.out.println("Car has already passed this checkpoint.");
            return;
        }

        System.out.println("Passed the checkpoint from the correct direction");
        if (hasPassedRequiredCheckpoints(car, checkpoint)) {
            System.out.println("Has passed previous expected checkpoints. All OK!");
            checkpointHistory.put(checkpoint, car);
        } else {
            System.out.println("Car hasn't passed required checkpoints. Ignoring.");
        }
    }

    private boolean hasPassedRequiredCheckpoints(Car car, Checkpoint checkpoint) {
        int currentCheckpointIndex = checkpoints.indexOf(checkpoint);

        if (currentCheckpointIndex == 0) {
            return true;
        }

        Checkpoint previousCheckpoint = checkpoints.get(currentCheckpointIndex - 1);
        if (checkpointHistory.get(previousCheckpoint) == null) {
            return false;
        }

        return true;
    }

}
