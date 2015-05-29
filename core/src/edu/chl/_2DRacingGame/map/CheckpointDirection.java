package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents possible directions which an object can enter a checkpoint.
 * "WEST" represents entering FROM west, i.e "-->"
 *
 * @author Daniel Sunnerberg
 */
public enum CheckpointDirection {
    WEST {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.x < endContactPosition.x;
        }
    },

    NORTH {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.y > endContactPosition.y;
        }
    },

    EAST {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.x > endContactPosition.x;
        }
    },

    SOUTH {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.y < endContactPosition.y;
        }
    };

    public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
        throw new UnsupportedOperationException("New valid directions must override this method before usage.");
    }

    private static CheckpointDirection getDirectionFromName(String name) {
        switch (name.toUpperCase()) {
            case "WEST":
                return WEST;
            case "NORTH":
                return NORTH;
            case "EAST":
                return EAST;
            case "SOUTH":
                return SOUTH;
            default:
                throw new IllegalArgumentException("Illegal direction name.");
        }
    }

    /**
     * @return all checkpoint directions found in argument, separated by "|"
     */
    public static List<CheckpointDirection> getDirectionsFromNames(String names) {
        List<CheckpointDirection> directions = new ArrayList<>();

        if (! names.contains("|")) {
            directions.add(getDirectionFromName(names));
            return directions;
        }

        for (String name : names.split("\\|")) {
            directions.add(getDirectionFromName(name));
        }
        return directions;
    }
}
