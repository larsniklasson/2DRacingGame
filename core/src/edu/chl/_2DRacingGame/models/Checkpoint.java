package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a checkpoint which can be passed in different specified directions. Every checkpoint has a type,
 * specifying its purpose.
 *
 * Important note: This class is NOT coupled with any texture/body, this is done separately.
 * For automated coupling with a body, see CheckpointFactory.
 *
 * @author Daniel Sunnerberg
 */
public class Checkpoint {

    private final CheckpointType type;
    private final List<CheckpointDirection> allowedPassingDirections = new ArrayList<>();

    /**
     * Creates a new, checkpoint which is classed as invisible.
     */
    public Checkpoint() {
        this(CheckpointType.INVISIBLE);
    }

    /**
     * Creates a new checkpoint with the specified type
     *
     * @param type the checkpoints type
     */
    public Checkpoint(CheckpointType type) {
        this.type = type;
    }

    /**
     * Makes it possible to pass the checkpoint from the specified direction.
     *
     * @param direction direction to allow
     */
    public void addAllowedPassingDirection(CheckpointDirection direction) {
        allowedPassingDirections.add(direction);
    }

    /**
     * Returns whether the passing is valid, considering all allowed passing directions.
     *
     * @param beginContactPosition position when entered the checkpoint
     * @param endContactPosition position when exited the checkpoint
     * @return
     */
    public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
        for (CheckpointDirection direction : allowedPassingDirections) {
            if (direction.isValidEntry(beginContactPosition, endContactPosition)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the checkpoints type
     */
    public CheckpointType getType() {
        return type;
    }

}
