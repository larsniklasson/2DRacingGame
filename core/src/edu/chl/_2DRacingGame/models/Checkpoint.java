package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Important note: This class is NOT coupled with any texture/body, this is done separately.
 * For automated coupling with a body, see CheckpointFactory.
 *
 * @author Daniel Sunnerberg
 */
public class Checkpoint {

    private final CheckpointType type;
    private final List<CheckpointDirection> allowedPassingDirections = new ArrayList<>();

    public Checkpoint() {
        this(CheckpointType.INVISIBLE);
    }

    public Checkpoint(CheckpointType type) {
        this.type = type;
    }

    public void addAllowedPassingDirection(CheckpointDirection direction) {
        allowedPassingDirections.add(direction);
    }

    public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
        for (CheckpointDirection direction : allowedPassingDirections) {
            if (direction.isValidEntry(beginContactPosition, endContactPosition)) {
                return true;
            }
        }

        return false;
    }

    public CheckpointType getType() {
        return type;
    }

}
