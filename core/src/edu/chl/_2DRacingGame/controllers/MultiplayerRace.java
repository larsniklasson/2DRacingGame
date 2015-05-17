package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScoreBoard;
import edu.chl._2DRacingGame.screens.ErrorScreen;
import edu.chl._2DRacingGame.screens.MultiplayerRaceFinishedScreen;
import edu.chl._2DRacingGame.screens.MainMenuDisplayer;
import edu.chl._2DRacingGame.world.GameWorld;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public class MultiplayerRace extends RaceController implements MultiplayerSetupListener, MainMenuDisplayer, OpponentListener {

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
    public void raceFinished(double time, String message) {
        scoreBoard.addResult(getPlayer(), time);
        Gdx.app.postRunnable(() -> {
            gameController.setScreen(new MultiplayerRaceFinishedScreen(getPlayer(), scoreBoard, this));
        });
    }

    @Override
    public void raceReady(String roomId, WarpClient client, List<Player> players) {
        Gdx.app.postRunnable(() -> {
            GameWorld world = getWorld();

            for (Player player : players) {
                if (player.getVehicle() == null) {
                    player.setVehicle(VehicleFactory.createVehicle(player.getVehicleType(), world.getb2World()));
                }
            }

            Player player = getPlayer();

            worldSyncer = new MultiplayerWorldSyncer(roomId, client, player, players);
            worldSyncer.addOpponentListener(this);

            getMode().addListener(worldSyncer);
            world.addUpdateListener(worldSyncer);

            scoreBoard.trackPlayers(players);

            world.addPlayers(players);
            world.spawnPlayers();

            gameController.setScreen(getScreen());
        });
    }

    /**
     * Called when we failed to find opponents for the race.
     *
     * @param message
     */
    @Override
    public void connectionError(String message) {
        Gdx.app.error("MultiplayerRace", "Failed to setup multiplayer race. Error message: " + message);
        ErrorScreen errorScreen = new ErrorScreen(
            "Failed to start multiplayer game",
            "Please check your connection and try again.",
            this
        );
        gameController.setScreen(errorScreen);
    }

    @Override
    public void restartRace() {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public void displayMainMenu() {
        getWorld().removeUpdateListener(worldSyncer);
        worldSyncer.disconnect();
        gameController.displayStartMenu();
    }

    private void hideVehicle(Player player) {
        // Since removing bodies from the world while it is active is a potentially dangerous action
        // according to documentation, we will simply move it outside the map.
        player.getActor().clearActions();
        player.getActor().addAction(Actions.sequence(Actions.delay(0.2f), Actions.moveTo(-10, -10, 0)));
    }

    @Override
    public void opponentFinished(Player player, double time) {
        scoreBoard.addResult(player, time);
        hideVehicle(player);
    }
}
