package edu.chl._2DRacingGame.controllers;

/**
 * Created by Anton on 2015-05-20.
 */
public interface SetUpListener {
    void setUpRace(String vehicleType, String mapName,String difficulty,  int laps, int opponents);
    void displayMainMenu();
}
