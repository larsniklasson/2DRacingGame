package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.controllers.MainMenuDisplayer;

/**
 * Created by Victor Christoffersson on 2015-05-19.
 */
public interface MultiPlayerMenuListener extends MainMenuDisplayer {
    void findOpponents(String vehicle, String map);
    void cancelSearch();
    void searchAgain();
}
