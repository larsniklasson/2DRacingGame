package edu.chl._2DRacingGame.controllers;

import edu.chl._2DRacingGame.controllers.MainMenuDisplayer;

/**
 * A class that manages searches for opponents in multiplayer.
 *
 * @author Victor Christoffersson
 */
public interface MultiPlayerMenuListener extends MainMenuDisplayer {
    void findOpponents(String vehicle, String map);
    void cancelSearch();
    void searchAgain();
}
