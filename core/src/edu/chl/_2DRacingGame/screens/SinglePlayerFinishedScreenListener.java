package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.controllers.MainMenuDisplayer;

/**
 * Created by Anton on 2015-05-26.
 */
public interface SinglePlayerFinishedScreenListener extends MainMenuDisplayer {
    void restartRace();
    void exitGame();
}