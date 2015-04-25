package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents possible directions which an object can enter a checkpoint.
 * "WEST" represents entering FROM west, i.e "-->"
 *
 * @author Daniel Sunnerberg
 */
public enum CheckpointDirection {
    WEST,

    NORTH {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.x > endContactPosition.x;
        }
    },

    EAST,

    SOUTH {
        @Override
        public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
            return beginContactPosition.x < endContactPosition.x;
        }
    };

    public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
        throw new UnsupportedOperationException("New valid directions must override this method before usage.");
    }
}
