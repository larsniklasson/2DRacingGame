package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
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

    private final LapListener listener;

    /**
     * A list of the checkpoints in the map, in the order they are expected to be occured.
     */
    private final List<Checkpoint> checkpoints;

    /**
     * Stores whether a player has passed a specific checkpoint this lap.
     */
    private final Map<Checkpoint, Integer> checkpointHistory = new HashMap<>();


    public CheckpointController(LapListener listener, List<Checkpoint> mapCheckpoints) {
        this.listener = listener;
        this.checkpoints = mapCheckpoints;
    }

    public void invalidPassing(Checkpoint checkpoint) {
        Gdx.app.log("CheckpointController", "Invalid checkpoint passing.");
    }

    public void validPassing(Checkpoint checkpoint) {

        Gdx.app.log("CheckpointController", "Passed the checkpoint from the correct direction");
        if (hasPassedRequiredCheckpoints(checkpoint)) {
            System.out.println("Has passed previous expected checkpoints. All OK!");
            boolean isClosedSystemLap = isClosedSystem() && checkpoint.getType() == CheckpointType.LAP_START;
            if (! checkpointHistory.isEmpty() && (isClosedSystemLap || checkpoint.getType() == CheckpointType.LAP_END)) {
                checkpointHistory.clear();
                listener.lap();
            }
            checkpointHistory.put(checkpoint, 1);
        } else {
            Gdx.app.log("CheckpointController", "Car hasn't passed required checkpoints. Ignoring.");
        }
    }

    private boolean hasPassedRequiredCheckpoints(Checkpoint checkpoint) {
        int currentCheckpointIndex = checkpoints.indexOf(checkpoint);

        boolean hasDrivenFullLap = checkpoints.size() == checkpointHistory.size();
        boolean justStartedLap = checkpointHistory.size() == 0 && currentCheckpointIndex == 0;
        if (hasDrivenFullLap || justStartedLap) {
            return true;
        }

        // If the user has reversed over the finish line and driven back over it
        if (! hasDrivenFullLap && currentCheckpointIndex == 0) {
            return false;
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
