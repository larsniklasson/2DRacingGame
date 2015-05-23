package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.GameController;
import edu.chl._2DRacingGame.controllers.MultiplayerRace;
import edu.chl._2DRacingGame.controllers.RaceController;
import edu.chl._2DRacingGame.controllers.SinglePlayerRace;
import edu.chl._2DRacingGame.models.Settings;
import edu.chl._2DRacingGame.persistance.DiskPersistor;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.screens.*;
import edu.chl._2DRacingGame.world.GameWorld;

import java.awt.*;
import java.util.Map;

/**
 * The absolute starting point for the application.
 * Responsible for displaying the correct screen, storing settings etc.
 */
public class _2DRacingGame extends Game implements GameController, MainMenuListener, OptionsScreenListener {

    private Settings settings;
    private RaceController raceController;

    @Override
    public void create() {
        Gdx.app.log("_2DRacingGame", "created");
        Assets.load();

        Persistor<Map<String, String>> persistor = new DiskPersistor<>();
        settings = new Settings(persistor);
        settings.load();

        displayStartMenu();
    }

    private void startSinglePlayer() {
        raceController = new SinglePlayerRace(this);
        raceController.setUp();
    }

    private void startMultiplayer() {
        raceController = new MultiplayerRace(this);
        raceController.setUp();
    }

    public GameWorld getGameWorld() {
        return raceController.getWorld();
    }

    @Override
    public void dispose() {
        if (raceController != null) {
            raceController.dispose();
        }
    }

    @Override
    public void displayStartMenu() {
        Gdx.app.postRunnable(() -> setScreen(new MainMenuScreen(this)));
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void applyNewOptions(float sound, Boolean fullscreen) {
        // TODO use this.settings
        System.out.println("Sound set to: " + sound);
        System.out.println("Fullscreen set to: " + fullscreen);

        Gdx.graphics.setDisplayMode(1280, 704, fullscreen);
    }

    public void restartRace() {
        raceController.restartRace();
    }

    @Override
    public void displaySinglePlayerMenuScreen() {startSinglePlayer();}

    @Override
    public void displayMultiPlayerMenuScreen() { startMultiplayer(); }

    @Override
    public void displayOptionsScreen() {setScreen(new OptionsMenuScreen(this));}

    @Override
    public void exitGame() {Gdx.app.exit();}


}