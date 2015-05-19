package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.MapScoresPersistor;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.steering.*;

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
        setRaceProperties(GameMap.PLACEHOLDER_MAP, new TimeTrial(2, this));

        scoresPersistor = new MapScoresPersistor(getMap(), getMode());
        scoresPersistor.findInstance();

        OurVehicle vehicle = new Car(getWorld().getb2World());
        vehicle.setSteeringSystem(new TireSteeringSystem(vehicle, new PlayerOneInputListener())); //TODO make all this stuff in VehicleFactory
        getPlayer().setVehicle(vehicle);
    }

    private void startRace() {
        getWorld().addPlayer(getPlayer());

        /*OurVehicle vehicle = new MotorCycle(getWorld().getb2World());
        vehicle.setSteeringSystem(new TireSteeringSystem(vehicle, new PlayerTwoInputListener()));
        Player p2 = new Player("apa", vehicle);
        p2.setIsControlledByClient(true);
        getWorld().addPlayer(p2);*/

        //testing adding ai-vehicles
        for(int i = 0; i < 5; i ++){
            OurVehicle ov = (OurVehicle) VehicleFactory.createRandomVehicle(getWorld().getb2World()); //TODO add random value to speed to make race more interesting

            Difficulty d = Difficulty.values()[(int)(Math.random()*Difficulty.values().length)];

            ov.setSteeringSystem(new WayPointSystem(ov, getWorld().wayPoints, d));

            Player p = new Player("p" + i, ov);
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

}
