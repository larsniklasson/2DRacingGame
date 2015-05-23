package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Screen;
import edu.chl._2DRacingGame.models.Settings;

/**
 * @author Daniel Sunnerberg
 */
public interface GameController {
    void setScreen(Screen screen);
    void displayStartMenu();
    Settings getSettings();
}
