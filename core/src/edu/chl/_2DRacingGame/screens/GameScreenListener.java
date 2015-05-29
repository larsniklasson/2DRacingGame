package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.screens.MainMenuDisplayer;

/**
 * @author Anton Ingvarsson
 */
public interface GameScreenListener extends MainMenuDisplayer {
    void resume();
    void pause();
    boolean isPauseable();


}
