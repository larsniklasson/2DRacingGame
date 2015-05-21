package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.MapScoresPersistor;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.SinglePlayerMenuScreen;
import edu.chl._2DRacingGame.steering.*;

/**
 * Controls the process surrounding a single player race, such as choosing map/vehicle/...,
 * controlling the logic flow between modules/other controllers, etc.
 *
 * @author Daniel Sunnerberg
 */
public class SinglePlayerRace extends RaceController implements setUpListener{

    private MapScoresPersistor scoresPersistor;

    /**
     * Creates a new SinglePlayerRace instance which bases itself on the specified gameController.
     *
     * @param gameController Game basis
     */
    public SinglePlayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        // TODO these should be chosen through in-game menu later
        gameController.setScreen(new SinglePlayerMenuScreen(this));
    }

    private void startRace() {
        getWorld().addPlayer(getPlayer());

        //New player-controlled random vehicle. using player2-controls
        Vehicle v = VehicleFactory.createPlayerVehicle(getWorld(), VehicleFactory.RANDOM_VEHICLE, 2);

        getWorld().addPlayer(new Player("p2", v));

        //testing adding ai-vehicles
        // TODO extract to separate method and call for it in setUpRace
        for(int i = 0; i < 5; i ++){

            // how to use VehicleFactory

            String s = VehicleFactory.RANDOM_VEHICLE;
            Difficulty d = Difficulty.getRandomDifficulty();

            Vehicle ai_v = VehicleFactory.createAIVehicle(getWorld(), s, d);

            Player p = new Player("p" + i, ai_v);
            getWorld().addPlayer(p);

            //OR le epic oneliner
            //getWorld().addPlayer(new Player("ai " + i, VehicleFactory.createAIVehicle(getWorld(), VehicleFactory.RANDOM_VEHICLE, Difficulty.getRandomDifficulty())));
        }

        getWorld().spawnPlayers();
        gameController.setScreen(getScreen());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        requestRaceSettings();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restartRace() {
        // TODO
        requestRaceSettings();
        startRace();
    }

    private void saveScore(double score) {
        MapScores scores = scoresPersistor.getInstance();
        scores.addScore(score);
        scoresPersistor.persistInstance();
    }

    /**
     * Callback for when the player finishes the race.
     *
     * @param score score determined by the game mode
     * @param message message with details from the game mode
     */
    @Override
    public void raceFinished(double score, String message) {
        saveScore(score);

        MapScores mapScores = scoresPersistor.getInstance();
        Gdx.app.log("SinglePlayerRace", "You finished the race in: " + score + " seconds.");
        if (mapScores.isHighScore(score)) {
            Gdx.app.log("SinglePlayerRace", "Highscore!");
        } else {
            Gdx.app.log("SinglePlayerRace", "Not a highscore. Current highscore: " + mapScores.getHighScore());
        }

        // TODO probably shouldn't restart by default
        restartRace();
    }

    @Override
    public void setUpRace(String vehicleType, String mapName, int laps, int opponents) {


        setRaceProperties(GameMap.PLACEHOLDER_MAP, new TimeTrial(laps, this));

        scoresPersistor = new MapScoresPersistor(getMap(), getMode());
        scoresPersistor.findInstance();

        Vehicle vehicle = VehicleFactory.createPlayerVehicle(getWorld(), VehicleFactory.CAR, 1);
        getPlayer().setVehicle(vehicle);
        startRace();
    }
}
