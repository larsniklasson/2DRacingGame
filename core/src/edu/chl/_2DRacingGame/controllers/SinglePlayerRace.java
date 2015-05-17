package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.MapScoresPersistor;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * @author Daniel Sunnerberg
 *         TODO race -> game?
 */
public class SinglePlayerRace extends RaceController {

    private MapScoresPersistor scoresPersistor;

    public SinglePlayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        // TODO these should be chosen through in-game menu later
        setRaceProperties(GameMap.PLACEHOLDER_MAP, new TimeTrial(this));

        scoresPersistor = new MapScoresPersistor(getMap(), getMode());
        scoresPersistor.findInstance();

        Vehicle vehicle = new Car(getWorld().getb2World());
        getPlayer().setVehicle(vehicle);
    }

    private void startRace() {
        getWorld().addPlayer(getPlayer());

        //testing adding ai-vehicles
        for(int i = 0; i < 5; i ++){
            OurVehicle ov = new Car(getWorld().getb2World()); //TODO add random value to speed to make race more interesting

            Difficulty d = Difficulty.values()[(int)(Math.random()*Difficulty.values().length)];

            ov.setSteeringSystem(new WayPointSystem(ov, getWorld().wayPoints, d));

            Player p = new Player("apa", ov);
            p.setIsControlledByClient(true); //TODO well, not rly.
            getWorld().addPlayer(p);
        }


        getWorld().spawnPlayers();
        gameController.setScreen(getScreen());
    }

    @Override
    public void setUp() {
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
    }

}
