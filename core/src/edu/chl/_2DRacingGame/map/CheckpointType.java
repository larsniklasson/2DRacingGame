package edu.chl._2DRacingGame.map;

/**
 * All possible checkpoint types.
 *
 * @author Daniel Sunnerberg
 */
public enum CheckpointType {
    INVISIBLE,
    LAP_START,
    LAP_END;

    public static CheckpointType getTypeFromName(String name) {
        if (name == null) {
            return INVISIBLE;
        }

        for (CheckpointType type : CheckpointType.values()) {
            if (name.toUpperCase().equals(type.name())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Found no matching type.");
    }
}
