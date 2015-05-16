package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame._2DRacingGame;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.MapScoresPersistor;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * @author Daniel Sunnerberg
 *         <p>
 *         TODO extract out commons
 *         TODO race -> game?
 */
public class SinglePlayerRace extends RaceController {

    private MapScoresPersistor scoresPersistor;
    private GameWorld world;

    public SinglePlayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        // TODO these should be chosen through in-game menu later
        map = GameMap.PLACEHOLDER_MAP;
        mode = new TimeTrial(this);
        scoresPersistor = new MapScoresPersistor(map, mode);
        scoresPersistor.findInstance();

        world = new GameWorld(map, mode);

        Vehicle vehicle = new Car(world.getb2World());
        player.setVehicle(vehicle);

        screen = new GameScreen(world);
    }

    private void startRace() {
        world.addPlayer(player);
        world.spawnPlayers();
        gameController.setScreen(screen);
    }

    @Override
    public void giveControl() {
        requestRaceSettings();
        startRace();
    }

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

        restartRace();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }

    @Override
    public GameWorld getWorld() {
        return world;
    }
}
