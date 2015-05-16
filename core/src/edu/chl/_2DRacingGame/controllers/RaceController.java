package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.RaceListener;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * @author Daniel Sunnerberg
 *
 * TODO remove Gdx.app.thread-ish?
 */
public abstract class RaceController implements RaceListener, Disposable {

    protected final Player player;
    protected GameMap map;

    protected GameMode mode;
    protected GameScreen screen;

    protected final GameController gameController;

    public RaceController(GameController gameController) {
        this.gameController = gameController;

        player = new Player();
        player.setIsControlledByClient(true);
    }

    // TODO name
    public abstract void giveControl();

    @Override
    public void dispose() {
        screen.dispose();
    }

    // TODO?
    public abstract GameWorld getWorld();

    public abstract void restartRace();
}
