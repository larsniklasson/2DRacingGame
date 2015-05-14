package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.MultiplayerSetupController;
import edu.chl._2DRacingGame.gameModes.GameListener;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.VehicleFactory;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.MapScoresPersistor;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.screens.MainMenuScreen;
import edu.chl._2DRacingGame.screens.MultiplayerRaceFinishedScreen;
import edu.chl._2DRacingGame.screens.RaceSummaryListener;
import edu.chl._2DRacingGame.world.GameWorld;
import edu.chl._2DRacingGame.world.MultiplayerGameWorld;

public class _2DRacingGame extends Game implements GameListener, RaceSummaryListener {

    private Player player;
    private GameMode gameMode;
    private GameMap gameMap;
    private GameScreen screen;
    private MapScoresPersistor mapScoresPersistor;
    private GameWorld gameWorld;

    private final boolean useMultiplayer = false;

    @Override
	public void create() {
		Gdx.app.log("_2DRacingGame", "created");
		Assets.load();

        player = new Player();
        player.setIsControlledByClient(true);

        setupExampleRace();
        if (useMultiplayer) {
            startMultiPlayer();
        } else {
            startSinglePlayer();
        }
	}

    private void startSinglePlayer() {
        gameWorld.addPlayer(player);
        gameWorld.spawnPlayers();
        setScreen(screen);
    }

    private void startMultiPlayer() {
        new MultiplayerSetupController(player, (client, players) -> {
            Gdx.app.postRunnable(() -> {

                for (Player player : players) {
                    if (player.getVehicle() == null) {
                        player.setVehicle(VehicleFactory.createVehicle(player.getVehicleType(), gameWorld.getb2World()));
                    }
                }

                ((MultiplayerGameWorld) gameWorld).setWarpClient(client);
                ((MultiplayerGameWorld) gameWorld).setClientPlayer(player);
                gameWorld.addPlayers(players);
                gameWorld.spawnPlayers();

                setScreen(screen);
            });
        }).findRace();
    }

    private void setupExampleRace() {
		// TODO these should be chosen through in-game menu later
        gameMap = GameMap.PLACEHOLDER_MAP;
        gameMode = new TimeTrial(this);
        mapScoresPersistor = new MapScoresPersistor(gameMap, gameMode);
        mapScoresPersistor.findInstance();

        if (useMultiplayer) {
            gameWorld = new MultiplayerGameWorld(gameMap, gameMode);
        } else {
            gameWorld = new GameWorld(gameMap, gameMode);
        }

        Vehicle vehicle = new MotorCycle(gameWorld.getb2World());
        player.setVehicle(vehicle);

        screen = new GameScreen(gameWorld);
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    @Override
	public void gameFinished(double score, String message) {
        Gdx.app.log("_2DRacingGame", "Race completed!");

        if (gameWorld instanceof MultiplayerGameWorld) {
            processMultiplayerFinish();
        } else {
            processSinglePlayerFinish(score);
        }

        MapScores scores = mapScoresPersistor.getInstance();
        scores.addScore(score);
        mapScoresPersistor.persistInstance();
    }

    private void processMultiplayerFinish() {
        MultiplayerGameWorld multiplayerGameWorld = (MultiplayerGameWorld) gameWorld;
        Gdx.app.postRunnable(() -> {
            setScreen(new MultiplayerRaceFinishedScreen(player, multiplayerGameWorld.getScoreBoard(), this));
        });
    }

    private void processSinglePlayerFinish(double score) {
        MapScores mapScores = mapScoresPersistor.getInstance();
        Gdx.app.log("_2DRacingGame", "You finished the race in: " + score + " seconds.");
        if (mapScores.isHighScore(score)) {
            Gdx.app.log("_2DRacingGame", "Highscore!");
        } else {
            Gdx.app.log("_2DRacingGame", "Not a highscore. Current highscore: " + mapScores.getHighScore());
        }

        restartRace();
    }

    @Override
    public void restartRace() {
        Gdx.app.postRunnable(() -> {
            // TODO doesn't work with multiplayer
            setupExampleRace();
            startSinglePlayer();
        });
    }

    @Override
    public void displayMainMenu() {
        Gdx.app.postRunnable(() -> {
            setScreen(new MainMenuScreen(this));
        });
    }

    @Override
    public void dispose() {
        screen.dispose();
    }

}
