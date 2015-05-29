package edu.chl._2DRacingGame.gameModes;

/**
 * Signifies a class who wants to receive events when a lap has been completed.
 *
 * @author Daniel Sunnerberg
 */
public interface LapListener {

    /**
     * Called when the player has passed through required checkpoints
     * and driven a lap around the map.
     */
    void lap();
}
