package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.gameModes.GameListener;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.screens.GameScreen;

public class _2DRacingGame extends Game implements GameListener {

	private GameMode gameMode;
	private GameMap gameMap;
	private GameScreen screen;

	@Override
	public void create() {
		Gdx.app.log("_2DRacingGame", "created");
		Assets.load();

		// TODO these should be chosen through in-game menu later
		gameMode = new TimeTrial(this);
		gameMap = GameMap.PLACEHOLDER_MAP;
		screen = new GameScreen(gameMap, gameMode);
		setScreen(screen); //this should be MainMenuScreen later
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	private void restart() {
		screen.dispose();
		screen = new GameScreen(gameMap, gameMode);
		setScreen(screen);
	}

	@Override
	public void gameFinished(String message) {
		System.out.println("GAME DONE: " + message); // TODO display restart screen such
		restart();

	}
}
