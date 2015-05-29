package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.WheeledVehicle;
import edu.chl._2DRacingGame.gameObjects.VehicleFactory;
import edu.chl._2DRacingGame.map.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScoreBoard;
import edu.chl._2DRacingGame.models.Settings;
import edu.chl._2DRacingGame.screens.*;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.List;

/**
 * Controls the process surrounding a multiplayer race, such as choosing map/vehicle/...,
 * finding opponents, controlling the logic flow between modules/other controllers, etc.
 *
 * @author Daniel Sunnerberg
 * @author Victor Christoffersson
 */
public class MultiplayerRace extends RaceController implements MultiplayerRaceFinderListener, MainMenuDisplayer, OpponentListener, MultiPlayerMenuListener {

    private MultiplayerWorldSyncer worldSyncer;
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private MultiplayerRaceFinder multiplayerRaceFinder;
    private SearchingForPlayerScreen searchingForPlayerScreen;

    /**
     * Creates a new MultiplayerRace instance which bases itself on the specified gameController.
     *
     * @param gameController Game basis
     */
    public MultiplayerRace(GameController gameController) {
        super(gameController);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        gameController.setScreen(new MultiPlayerMenuScreen(this));
    }

    /**
     * Callback for when our client player has finished the race.
     * Displays a screen which shows the opponents race times as they finish.
     *
     * @param time time it took to finish the race
     * @param message message from the game mode
     */
    @Override
    public void raceFinished(double time, String message) {
        scoreBoard.addResult(getPlayer(), time);
        Gdx.app.postRunnable(() -> {
            gameController.setScreen(new MultiplayerRaceFinishedScreen(getPlayer(), scoreBoard, this));
        });
    }

    /**
     * Callback for when we have found enough opponents and have joined an online room with them.
     *
     * @param roomId id of the joined room
     * @param client AppWarp-client for their API
     * @param players list of opponents
     */
    @Override
    public void raceReady(String roomId, WarpClient client, List<Player> players) {
        Gdx.app.postRunnable(() -> {
            GameWorld world = getWorld();

            for (Player player : players) {
                if (player.getVehicle() == null) {
                    player.setVehicle(VehicleFactory.createVehicle(world.getb2World(), player.getVehicleType()));
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
     * Callback for when we failed to find opponents for the race.
     *
     * @param message message describing the error
     */
    @Override
    public void connectionError(String message) {
        Gdx.app.error("MultiplayerRace", "Failed to setup multiplayer race. Error message: " + message);
        Gdx.app.postRunnable(() -> {
            searchingForPlayerScreen.displayErrorInfo();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restartRace() {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Disconnects from AppWarp-servers and displays the main menu.
     */
    @Override
    public void displayMainMenu() {
        disconnect();
        super.displayMainMenu();
    }

    /**
     * Disconnects from AppWarp.
     */
    private void disconnect() {
        if (worldSyncer != null) {
            getWorld().removeUpdateListener(worldSyncer);
            worldSyncer.disconnect();
        }

        if(multiplayerRaceFinder != null){
            multiplayerRaceFinder.disconnect();
        }
    }

    private void hideVehicle(Player player) {
        // Since removing bodies from the world while it is active is a potentially dangerous action
        // according to documentation, we will simply move it outside the map.
        player.getVehicle().getActor().clearActions();
        float hideDelay = 0.2f;
        player.getVehicle().getActor().addAction(Actions.sequence(Actions.delay(hideDelay), Actions.moveTo(-10, -10, 0)));
    }

    /**
     * Callback for when an opponent has finished the race.
     *
     * @param opponent opponent who finished the race
     * @param time time it took for the opponent to finish
     */
    @Override
    public void opponentFinished(Player opponent, double time) {
        scoreBoard.addResult(opponent, time);
        hideVehicle(opponent);
    }

    /**
     * Finds opponents based on chosen map.
     *
     * @param chosenVehicle the chosen vehicle
     * @param chosenMap the chosen map to find opponents for
     */
    @Override
    public void findOpponents(String chosenVehicle, String chosenMap) {
        setRaceSettings(chosenVehicle, chosenMap);
        findOpponents();
    }

    /**
     * Finds opponents based on previously set map.
     */
    private void findOpponents() {
        searchingForPlayerScreen = new SearchingForPlayerScreen(this);
        gameController.setScreen(searchingForPlayerScreen);

        Settings settings = gameController.getSettings();
        String apiKey = settings.getSetting("APPWARP_API_KEY");
        String secretKey = settings.getSetting("APPWARP_SECRET_KEY");
        int desiredOpponents = 2; // Should probably be chosen through UI in the future
        multiplayerRaceFinder = new MultiplayerRaceFinder(apiKey, secretKey, desiredOpponents, this);
        multiplayerRaceFinder.setPreferences(getPlayer(), getMap());
        multiplayerRaceFinder.findRace();
    }

    @Override
    public void cancelSearch() {
        disconnect();
        setUp();
        Gdx.app.log("Search cancelled by user", "Returning to menu");
    }

    @Override
    public void searchAgain() {
        disconnect();
        Gdx.app.log("MultiplayerRace", "Creating a new RaceFinder based on set properties.");
        findOpponents();
    }

    private void setRaceSettings(String chosenVehicle, String chosenMap){
        setRaceProperties(GameMap.valueOf(chosenMap), new TimeTrial(2, this));

        WheeledVehicle vehicle = VehicleFactory.createPlayerVehicle(getWorld().getb2World(), chosenVehicle, 1);
        getPlayer().setVehicle(vehicle);
    }

    @Override
    public boolean isPauseable() {
        return false;
    }
}
