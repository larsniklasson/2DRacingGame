package edu.chl._2DRacingGame.gameModes;

/**
 * @author Daniel Sunnerberg
 */
public interface LapListener {

    /**
     * Called when the player has passed through required checkpoints
     * and driven a lap around the map.
     */
    void lap();
}
