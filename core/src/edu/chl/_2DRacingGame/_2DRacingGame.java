package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.GameController;
import edu.chl._2DRacingGame.controllers.MultiplayerRace;
import edu.chl._2DRacingGame.controllers.RaceController;
import edu.chl._2DRacingGame.controllers.SinglePlayerRace;
import edu.chl._2DRacingGame.screens.MainMenu;
import edu.chl._2DRacingGame.world.GameWorld;

public class _2DRacingGame extends Game implements GameController {

    // TODO
    private final boolean useMultiplayer = false;

    private RaceController raceController;

    @Override
    public void create() {

        Gdx.app.log("_2DRacingGame", "created");
        Assets.load();

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
        Gdx.app.postRunnable(() -> setScreen(new MainMenu(this)));
    }

    public void restartRace() {
        raceController.restartRace();
    }
}
