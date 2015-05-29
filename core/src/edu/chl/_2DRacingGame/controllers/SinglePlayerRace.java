package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.Sets;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.VehicleFactory;
import edu.chl._2DRacingGame.map.GameMap;
import edu.chl._2DRacingGame.models.*;
import edu.chl._2DRacingGame.persistance.DiskPersistor;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.screens.SinglePlayerFinishedScreen;
import edu.chl._2DRacingGame.screens.SinglePlayerFinishedScreenListener;
import edu.chl._2DRacingGame.screens.SinglePlayerMenuScreen;
import edu.chl._2DRacingGame.gameObjects.steering.*;

import java.util.List;
import java.util.Set;

/**
 * Controls the process surrounding a single player race, such as choosing map/vehicle/...,
 * controlling the logic flow between modules/other controllers, etc.
 *
 * Be aware that when settings screens/using the world etc, LibGDX expects to be in its own thread.
 * This can be achieved by using: Gdx.app.postRunnable(...).
 *
 * @author Daniel Sunnerberg
 */
public class SinglePlayerRace extends RaceController implements SetUpListener, SinglePlayerFinishedScreenListener {

    private IdentifiableScores mapScores;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        requestRaceSettings();
    }

    private void startRace() {
        getWorld().addPlayer(getPlayer());
        getWorld().spawnPlayers();
        gameController.setScreen(getScreen());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restartRace() {
        requestRaceSettings();
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

    /**
     * Starts a new single player race with the specified options.
     *
     * @param vehicleType name of the selected vehicle
     * @param mapName name of the selected map
     * @param difficulty selected difficulty of opponents
     * @param nbrOfLaps how many laps the race consists of
     * @param nbrOfOpponents how many opponents the player wants
     */
    @Override
    public void startRace(String vehicleType, String mapName, String difficulty, int nbrOfLaps, int nbrOfOpponents) {
        setRaceProperties(GameMap.valueOf(mapName), new TimeTrial(nbrOfLaps, this));
        loadSavedScores();
        createAIOpponents(nbrOfOpponents, Difficulty.valueOf(difficulty));

        Vehicle vehicle = VehicleFactory.createPlayerVehicle(getWorld().getb2World(), vehicleType, 1);
        getPlayer().setVehicle(vehicle);
        startRace();
    }

    private void loadSavedScores() {
        if (getMap() == null || getMode() == null) {
            throw new IllegalStateException("A map and mode needs to be selected before loading saved scores.");
        }

        Set<Object> identifiers = Sets.newHashSet(getMap(), getMode());
        Persistor<List<Double>> persistor = new DiskPersistor<>();
        mapScores = new IdentifiableScores(identifiers, getMode().getScoreComparator(), persistor);
        mapScores.load();
    }

    private void createAIOpponents(int nbrOfOpponents, Difficulty d) {
        for(int i = 0; i < nbrOfOpponents; i ++) {
            Array<Vector2> wayPoints = getMap().getMapObjects().getWayPoints();
            Vehicle ai_v = VehicleFactory.createAIVehicle(getWorld().getb2World(), VehicleFactory.RANDOM_VEHICLE, d, wayPoints);
            Player p = new Player("p" + i, ai_v);
            getWorld().addPlayer(p);
        }
    }

    @Override
    public boolean isPauseable() {
        return true;
    }
}
