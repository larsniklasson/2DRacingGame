package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.gameModes.LapListener;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.Checkpoint;
import edu.chl._2DRacingGame.models.CheckpointType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointController {

    // TODO event-methods shouldn't specify Car/Vehicle, we're only handling
    // events for our local vehicle.


    private final LapListener listener;

    /**
     * A list of the checkpoints in the map, in the order they are expected to be occured.
     */
    private final List<Checkpoint> checkpoints;

    /**
     * Stores whether a player has passed a specific checkpoint this lap.
     */
    private final Map<Checkpoint, Vehicle> checkpointHistory = new HashMap<>();


    public CheckpointController(LapListener listener, List<Checkpoint> mapCheckpoints) {
        this.listener = listener;
        this.checkpoints = mapCheckpoints;
    }

    public void invalidPassing(Car car, Checkpoint checkpoint) {
        System.out.println("Invalid checkpoint passing.");
    }

    public void validPassing(Car car, Checkpoint checkpoint) {

        System.out.println("Passed the checkpoint from the correct direction");
        if (hasPassedRequiredCheckpoints(car, checkpoint)) {
            System.out.println("Has passed previous expected checkpoints. All OK!");
            boolean isClosedSystemLap = isClosedSystem() && checkpoint.getType() == CheckpointType.LAP_START;
            if (! checkpointHistory.isEmpty() && (isClosedSystemLap || checkpoint.getType() == CheckpointType.LAP_END)) {
                checkpointHistory.clear();
                listener.lap();
            }
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
        return checkpointHistory.get(previousCheckpoint) != null;

    }

    /**
     * Checks if the checkpoints only has a LAP_START which also functions as a
     * LAP_END.
     */
    private boolean isClosedSystem() {
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.getType() == CheckpointType.LAP_END) {
                return false;
            }
        }

        return true;
    }

}
