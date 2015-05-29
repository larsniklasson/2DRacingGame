package edu.chl._2DRacingGame.gameModes;

/**
 * Signifies a class who wants to receive events when a race has been completed.
 *
 * @author Daniel Sunnerberg
 */
public interface RaceListener {

    /**
     * Callback for when a race has been completed.
     *
     * @param score race score
     * @param message message from game mode
     */
    void raceFinished(double score, String message);
}
