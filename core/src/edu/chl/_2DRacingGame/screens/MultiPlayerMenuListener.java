package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.controllers.MainMenuDisplayer;

/**
 * @author Victor Christoffersson
 */
public interface MultiPlayerMenuListener extends MainMenuDisplayer {
    void findOpponents(String vehicle, String map);
    void cancelSearch();
    void searchAgain();
}
