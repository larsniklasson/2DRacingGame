package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.GameListener;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.MapScores;
import edu.chl._2DRacingGame.screens.GameScreen;

public class _2DRacingGame extends Game implements GameListener {

	private GameMode gameMode;
	private GameMap gameMap;
	private GameScreen screen;
	private MapScores mapScores;

	@Override
	public void create() {
		Gdx.app.log("_2DRacingGame", "created");
		Assets.load();

		setupExampleRace();
		setScreen(screen); //this should be MainMenuScreen later
	}

	private void setupExampleRace() {
		if (screen != null) {
			screen.dispose();
		}
		// TODO these should be chosen through in-game menu later
		gameMap = GameMap.PLACEHOLDER_MAP;
		gameMode = new TimeTrial(this);
		mapScores = MapScores.getInstance(gameMap, gameMode);
		screen = new GameScreen(gameMap, gameMode);
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	private void restart() {
		setupExampleRace();
		setScreen(screen);
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
}
