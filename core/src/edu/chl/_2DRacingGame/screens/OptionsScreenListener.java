package edu.chl._2DRacingGame.screens;

/**
 * Class that can display start menu and apply saved changes to settings.
 *
 * @author Victor Christoffersson
 */
public interface OptionsScreenListener {
    void displayStartMenu();
    void applyNewOptions(float sound, Boolean fullscreen);
}
