package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.GameController;
import edu.chl._2DRacingGame.controllers.MultiplayerRace;
import edu.chl._2DRacingGame.controllers.RaceController;
import edu.chl._2DRacingGame.controllers.SinglePlayerRace;
import edu.chl._2DRacingGame.screens.MainMenuListener;
import edu.chl._2DRacingGame.screens.MainMenuScreen;
import edu.chl._2DRacingGame.screens.OptionsMenuScreen;
import edu.chl._2DRacingGame.screens.SinglePlayerMenuScreen;
import edu.chl._2DRacingGame.world.GameWorld;

public class _2DRacingGame extends Game implements GameController, MainMenuListener {

    // TODO
    private final boolean useMultiplayer = false;

    private RaceController raceController;

    @Override
    public void create() {

        Gdx.app.log("_2DRacingGame", "created");
        Assets.load();
        displayStartMenu();
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
    public void displaySinglePlayerMenuScreen() {setScreen(new SinglePlayerMenuScreen());}

    @Override
    public void displayMultiPlayerMenuScreen() {setScreen(new SinglePlayerMenuScreen());}

    @Override
    public void displayOptionsScreen() {setScreen(new OptionsMenuScreen(this));}

    @Override
    public void exitGame() {Gdx.app.exit();}
}
