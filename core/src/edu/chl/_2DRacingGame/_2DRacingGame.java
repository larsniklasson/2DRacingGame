package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.*;
import edu.chl._2DRacingGame.vehicle.VehicleAssets;
import edu.chl._2DRacingGame.models.Settings;
import edu.chl._2DRacingGame.persistence.DiskPersistor;
import edu.chl._2DRacingGame.persistence.Persistor;
import edu.chl._2DRacingGame.screens.*;
import java.util.Map;

/**
 * The absolute starting point for the application.
 * Responsible for displaying the correct screen, storing settings etc.
 */
public class _2DRacingGame extends Game implements GameController, MainMenuListener, OptionsScreenListener {

    private Settings settings;

    @Override
    public void create() {
        Gdx.app.log("_2DRacingGame", "created");

        VehicleAssets.load();
        ScreenAssets.load();

        Persistor<Map<String, String>> persistor = new DiskPersistor<>();
        settings = new Settings(persistor);
        settings.load();

        displayStartMenu();
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
        // TODO implement settings (use this.settings)
        Gdx.graphics.setDisplayMode(1280, 704, fullscreen);
    }

    @Override
    public void displaySinglePlayerMenuScreen() {
        new SinglePlayerRace(this).setUp();
    }

    @Override
    public void displayMultiPlayerMenuScreen() {
        new MultiplayerRace(this).setUp();
    }

    @Override
    public void displayOptionsScreen() {setScreen(new OptionsMenuScreen(this));}

    @Override
    public void exitGame() {Gdx.app.exit();}

    @Override
    public void displayControlsMenuScreen() {
        setScreen(new ControlsMenuScreen(this));
    }


}