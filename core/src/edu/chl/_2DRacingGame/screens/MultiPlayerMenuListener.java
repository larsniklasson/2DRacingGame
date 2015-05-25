package edu.chl._2DRacingGame.screens;

/**
 * Created by Victor Christoffersson on 2015-05-19.
 */
public interface MultiPlayerMenuListener {
    void findOpponents(String vehicle, String map);
    void displayMainMenuScreen();
    void cancelSearch();
    void searchAgain();
}
