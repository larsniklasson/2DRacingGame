package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScoreBoard;
import edu.chl._2DRacingGame.screens.MultiplayerRaceFinishedScreen;
import edu.chl._2DRacingGame.screens.RaceSummaryListener;
import edu.chl._2DRacingGame.world.GameWorld;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * @author Daniel Sunnerberg
 *
 * TODO consistent spelling of multiplayer...
 */
public class MultiplayerRace extends RaceController implements MultiplayerSetupListener, RaceSummaryListener, OpponentListener {

    private MultiplayerWorldSyncer worldSyncer;
    private final ScoreBoard scoreBoard = new ScoreBoard();

    public MultiplayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        // TODO these should be chosen through in-game menu later
        setRaceProperties(GameMap.PLACEHOLDER_MAP, new TimeTrial(this));

        Vehicle vehicle = new Car(getWorld().getb2World());
        getPlayer().setVehicle(vehicle);
    }

    @Override
    public void setUp() {
        requestRaceSettings();
        new MultiplayerSetupController(getPlayer(), this).findRace();
    }

    @Override
    public void raceFinished(double score, String message) {
        Gdx.app.postRunnable(() -> {
            gameController.setScreen(new MultiplayerRaceFinishedScreen(getPlayer(), scoreBoard, this));
        });
    }

    @Override
    public void raceReady(WarpClient client, List<Player> players) {
        Gdx.app.postRunnable(() -> {
            GameWorld world = getWorld();

            for (Player player : players) {
                if (player.getVehicle() == null) {
                    player.setVehicle(VehicleFactory.createVehicle(player.getVehicleType(), world.getb2World()));
                }
            }

            Player player = getPlayer();

            worldSyncer = new MultiplayerWorldSyncer(client, player, players);
            worldSyncer.addOpponentListener(this);

            getMode().addListener(worldSyncer);
            world.addUpdateListener(worldSyncer);

            scoreBoard.trackPlayers(players);

            world.addPlayers(players);
            world.spawnPlayers();

            gameController.setScreen(getScreen());
        });
    }

    @Override
    public void restartRace() {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public void displayMainMenu() {
        gameController.displayStartMenu();
    }

    @Override
    public void opponentFinished(Player player, double time) {
        scoreBoard.addResult(player, time);
    }
}
