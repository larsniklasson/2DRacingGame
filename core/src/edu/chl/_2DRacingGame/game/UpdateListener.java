package edu.chl._2DRacingGame.game;

/**
 * A class which wants to be notified when the game has been updated.
 *
 * @author Daniel Sunnerberg
 */
public interface UpdateListener {

    /**
     * Callback for when the game has been updated.
     */
    void worldUpdated();

}
