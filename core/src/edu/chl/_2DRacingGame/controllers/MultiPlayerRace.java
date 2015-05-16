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
import edu.chl._2DRacingGame.screens.GameScreen;
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
public class MultiPlayerRace extends RaceController implements MultiplayerSetupListener, RaceSummaryListener, OpponentListener {

    private MultiplayerWorldSyncer worldSyncer;
    private final ScoreBoard scoreBoard = new ScoreBoard();

    public MultiPlayerRace(GameController gameController) {
        super(gameController);
    }

    private void requestRaceSettings() {
        // TODO these should be chosen through in-game menu later
        map = GameMap.PLACEHOLDER_MAP;
        mode = new TimeTrial(this);
        world = new GameWorld(map, mode);

        Vehicle vehicle = new Car(world.getb2World());
        player.setVehicle(vehicle);

        screen = new GameScreen(world);
    }

    @Override
    public void giveControl() {
        requestRaceSettings();
        new MultiplayerSetupController(player, this).findRace();
    }

    @Override
    public void raceFinished(double score, String message) {
        Gdx.app.postRunnable(() -> {
            gameController.setScreen(new MultiplayerRaceFinishedScreen(player, scoreBoard, this));
        });
    }

    @Override
    public void raceReady(WarpClient client, List<Player> players) {
        Gdx.app.postRunnable(() -> {

            for (Player player : players) {
                if (player.getVehicle() == null) {
                    player.setVehicle(VehicleFactory.createVehicle(player.getVehicleType(), world.getb2World()));
                }
            }

            worldSyncer = new MultiplayerWorldSyncer(client, player, players);
            mode.addListener(worldSyncer);

            scoreBoard.trackPlayers(players);

            world.addPlayers(players);
            world.spawnPlayers();

            gameController.setScreen(screen);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
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
