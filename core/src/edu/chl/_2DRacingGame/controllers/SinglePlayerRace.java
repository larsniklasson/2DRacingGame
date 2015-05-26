package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScoreList;
import edu.chl._2DRacingGame.persistance.DiskPersistor;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.screens.SinglePlayerFinishedScreen;
import edu.chl._2DRacingGame.screens.SinglePlayerMenuScreen;
import edu.chl._2DRacingGame.steering.*;
import sun.java2d.windows.GDIBlitLoops;

import java.util.List;

/**
 * Controls the process surrounding a single player race, such as choosing map/vehicle/...,
 * controlling the logic flow between modules/other controllers, etc.
 *
 * @author Daniel Sunnerberg
 */
public class SinglePlayerRace extends RaceController implements SetUpListener, SinglePlayerFinishedScreenListener{

    private MapScores mapScores;

    /**
     * Creates a new SinglePlayerRace instance which bases itself on the specified gameController.
     *
     * @param gameController Game basis
     */
    public SinglePlayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        gameController.setScreen(new SinglePlayerMenuScreen(this));
    }

    private void startRace() {
        getWorld().addPlayer(getPlayer());

        //New player-controlled random vehicle. using player2-controls
        //Vehicle v = VehicleFactory.createPlayerVehicle(getWorld(), VehicleFactory.RANDOM_VEHICLE, 2);

        //getWorld().addPlayer(new Player("p2", v));


        //testing adding ai-vehicles
        // TODO extract to separate method and call for it in setUpRace


            //OR le epic oneliner
            //getWorld().addPlayer(new Player("ai " + i, VehicleFactory.createAIVehicle(getWorld(), VehicleFactory.RANDOM_VEHICLE, Difficulty.getRandomDifficulty())));


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
        requestRaceSettings();
    }

    @Override
    public void exitGame() {
        Gdx.app.exit();
    }

    private void saveScore(double score) {
        ScoreList scores = mapScores.getScores();
        scores.addScore(score);
        mapScores.save();
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

        ScoreList scores = mapScores.getScores();
        Gdx.app.log("SinglePlayerRace", "You finished the race in: " + score + " seconds.");
        if (scores.isHighScore(score)) {
            Gdx.app.log("SinglePlayerRace", "Highscore!");
        } else {
            Gdx.app.log("SinglePlayerRace", "Not a highscore. Current highscore: " + scores.getHighScore());
        }

        Gdx.app.postRunnable(() -> gameController.setScreen(new SinglePlayerFinishedScreen(scores, score, this)));
    }

    @Override
    public void setUpRace(String vehicleType, String mapName,String difficulty, int nbrOfLaps, int nbrOfOpponents) {
        setRaceProperties(GameMap.valueOf(mapName), new TimeTrial(nbrOfLaps, this));

        Persistor<List<Double>> persistor = new DiskPersistor<>();
        mapScores = new MapScores(getMap(), getMode(), persistor);
        mapScores.load();

        createAIOpponents(nbrOfOpponents, Difficulty.valueOf(difficulty));

        Vehicle vehicle = VehicleFactory.createPlayerVehicle(getWorld(), vehicleType, 1);
        getPlayer().setVehicle(vehicle);
        startRace();
    }

    @Override
    public void displayMainMenu() {
        gameController.displayStartMenu();
    }



    private void createAIOpponents(int nbrOfOpponents, Difficulty d) {
        for(int i = 0; i < nbrOfOpponents; i ++) {
            Vehicle ai_v = VehicleFactory.createAIVehicle(getWorld(), VehicleFactory.RANDOM_VEHICLE, d);
            Player p = new Player("p" + i, ai_v);
            getWorld().addPlayer(p);
        }
    }
}
