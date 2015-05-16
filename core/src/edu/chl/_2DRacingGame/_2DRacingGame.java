package edu.chl._2DRacingGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import edu.chl._2DRacingGame.controllers.GameController;
import edu.chl._2DRacingGame.controllers.MultiPlayerRace;
import edu.chl._2DRacingGame.controllers.RaceController;
import edu.chl._2DRacingGame.controllers.SinglePlayerRace;
import edu.chl._2DRacingGame.screens.MainMenuScreen;
import edu.chl._2DRacingGame.world.GameWorld;

public class _2DRacingGame extends Game implements GameController {

    // TODO
    private final boolean useMultiplayer = true;

    private RaceController raceController;

    @Override
    public void create() {

        Gdx.app.log("_2DRacingGame", "created");
        Assets.load();

        if (useMultiplayer) {
            startMultiPlayer();
        } else {
            startSinglePlayer();
        }
    }

    private void startSinglePlayer() {
        raceController = new SinglePlayerRace(this);
        raceController.giveControl();
    }

    private void startMultiPlayer() {
        raceController = new MultiPlayerRace(this);
        raceController.giveControl();
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
}
