package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.screens.GameScreen;

public class _2DRacingGame extends Game {

	@Override
	public void create() {
		Gdx.app.log("_2DRacingGame", "created");
		setScreen(new GameScreen()); //this should be MainMenuScreen later
	}
}
