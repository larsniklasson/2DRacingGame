package edu.chl._2DRacingGame.screens;

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
