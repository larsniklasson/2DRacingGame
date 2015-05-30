package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.screens.MainMenuDisplayer;

/**
 *
 * A class that which is able to pause/resume the game
 *
 * @author Anton Ingvarsson
 */

public interface GameScreenListener extends MainMenuDisplayer {
    void resume();
    void pause();
    boolean isPauseable();


}
