package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import edu.chl._2DRacingGame.controllers.MultiplayerSetupController;
import edu.chl._2DRacingGame.gameModes.GameListener;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.world.GameWorld;
import edu.chl._2DRacingGame.world.MultiplayerGameWorld;

public class _2DRacingGame extends Game implements GameListener {

    private Player player;
    private GameMode gameMode;
    private GameMap gameMap;
    private GameScreen screen;
    private MapScores mapScores;
    private GameWorld gameWorld;

    private final boolean useMultiplayer = true;

    @Override
	public void create() {
		Gdx.app.log("_2DRacingGame", "created");
		Assets.load();

        player = new Player();
        setupExampleRace();

        if (useMultiplayer) {
            startMultiPlayer();
        } else {
            startSinglePlayer();
        }
	}

    private void startSinglePlayer() {
        setScreen(screen);
    }

    private void startMultiPlayer() {
        new MultiplayerSetupController(player, (client, opponents) -> {
            Gdx.app.postRunnable(() -> {
                gameWorld = new MultiplayerGameWorld(player, opponents, gameMap, gameMode, client);
                screen = new GameScreen(gameWorld);
                setScreen(screen);
            });
        }).findRace();
    }

    private void setupExampleRace() {
		// TODO these should be chosen through in-game menu later
		gameMap = GameMap.PLACEHOLDER_MAP;
		gameMode = new TimeTrial(this);
		mapScores = MapScores.getInstance(gameMap, gameMode);
        if (! useMultiplayer) {
            gameWorld = new GameWorld(player, gameMap, gameMode);
            screen = new GameScreen(gameWorld);
        }
	}

	private void restart() {
        // TODO doesn't work with multiplayer

        // Disposal of the previous screen has to be run on the GDX thread to prevent
        // JVM crash.
        Gdx.app.postRunnable(() -> {
            // Store the currently active screen and dispose it AFTER the new screen has been
            // created and set. Disposing too early allows for a race-case which can crash the application.
            Screen previousGameScreen = screen;
            setupExampleRace();
            setScreen(screen);
            previousGameScreen.dispose();
        });
	}

	@Override
	public void gameFinished(double score, String message) {
		Gdx.app.log("_2DRacingGame", "Game completed: " + message); // TODO display restart screen such
		if (mapScores.isHighScore(score)) {
			Gdx.app.log("_2DRacingGame", "Highscore!");
		} else {
			Gdx.app.log("_2DRacingGame", "Not a highscore. Current highscore: " + mapScores.getHighScore());
		}

		mapScores.addScore(score);
		mapScores.persist();
		restart();
	}

    @Override
    public void dispose() {
        screen.dispose();
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

}
