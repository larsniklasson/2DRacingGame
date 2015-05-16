package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.models.Player;

/**
 * @author Daniel Sunnerberg
 */
interface OpponentListener {
    void opponentFinished(Player player, double time);
}
