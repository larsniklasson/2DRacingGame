package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.GameController;
import edu.chl._2DRacingGame.controllers.MultiplayerRace;
import edu.chl._2DRacingGame.controllers.RaceController;
import edu.chl._2DRacingGame.controllers.SinglePlayerRace;
import edu.chl._2DRacingGame.screens.*;
import edu.chl._2DRacingGame.world.GameWorld;

public class _2DRacingGame extends Game implements GameController, MainMenuListener, MultiPlayerMenuListener {

    // TODO
    private final boolean useMultiplayer = false;

    private RaceController raceController;

    @Override
    public void create() {

        Gdx.app.log("_2DRacingGame", "created");
        Assets.load();
        //displayStartMenu();
        if (useMultiplayer) {
            startMultiplayer();
        } else {
            startSinglePlayer();
        }
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
        raceController.dispose();
    }

    @Override
    public void displayStartMenu() {
        Gdx.app.postRunnable(() -> setScreen(new MainMenuScreen(this)));
    }

    public void restartRace() {
        raceController.restartRace();
    }

    @Override
    public void displaySinglePlayerMenuScreen() {setScreen(new SinglePlayerMenuScreen(this));}

    @Override
    public void displayMultiPlayerMenuScreen() {setScreen(new MultipPlayerMenuScreen(this));}

    @Override
    public void displayOptionsScreen() {setScreen(new OptionsMenuScreen());}

    @Override
    public void exitGame() {Gdx.app.exit();}

    @Override
    public void startMultiplayerRace() {
        startMultiplayer();
    }

    @Override
    public void startSinglePlayerRace() {
        startMultiplayer();
    }

    @Override
    public void displayMainMenuScreen() {
        displayStartMenu();
    }
}
