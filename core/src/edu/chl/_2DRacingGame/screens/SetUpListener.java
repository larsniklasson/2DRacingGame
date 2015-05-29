package edu.chl._2DRacingGame.screens;

import edu.chl._2DRacingGame.screens.MainMenuDisplayer;

/**
 * @author Anton Ingvarsson
 */
public interface SetUpListener extends MainMenuDisplayer {
    void startRace(String vehicleType, String mapName, String difficulty, int laps, int opponents);
}
