package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.models.Player;

/**
 * A class who wishes to be notified when an opponent has finished the race.
 *
 * @author Daniel Sunnerberg
 */
interface OpponentListener {

    /**
     * Callback for when an opponent finished the race.
     *
     * @param opponent opponent who finished the race
     * @param time time it took to finish the race
     */
    void opponentFinished(Player opponent, double time);

}
