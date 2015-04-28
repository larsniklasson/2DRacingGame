package edu.chl._2DRacingGame.models;

/**
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
        switch (name.toUpperCase()) {
            case "LAP_START":
                return LAP_START;
            case "LAP_END":
                return LAP_END;
            case "INVISIBLE":
            default:
                throw new IllegalArgumentException("Illegal type name.");
        }
    }
}
