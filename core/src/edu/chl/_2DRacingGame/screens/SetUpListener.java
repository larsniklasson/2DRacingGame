package edu.chl._2DRacingGame.screens;

/**
 * Sets up the single player race
 * @author Anton Ingvarsson
 */
public interface SetUpListener extends MainMenuDisplayer {
    void startRace(String vehicleType, String mapName, String difficulty, int laps, int opponents);
}
