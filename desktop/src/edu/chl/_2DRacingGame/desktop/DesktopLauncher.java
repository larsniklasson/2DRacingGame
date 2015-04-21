package edu.chl._2DRacingGame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.chl._2DRacingGame._2DRacingGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "2DRacingGame";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new _2DRacingGame(), config);
	}
}
