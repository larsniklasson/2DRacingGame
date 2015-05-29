package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.Screen;
import edu.chl._2DRacingGame.models.Settings;

/**
 * Represents the most inner core of the Game, handling screen changes, exiting logic etc.
 *
 * @author Daniel Sunnerberg
 */
public interface GameController {

    /**
     * Replaces the active screen with the specified one.
     *
     * @param screen new screen
     */
    void setScreen(Screen screen);

    /**
     * Displays the start menu.
     */
    void displayStartMenu();

    /**
     * @return game settings
     */
    Settings getSettings();

    /**
     * Exits the game.
     */
    void exitGame();

}
