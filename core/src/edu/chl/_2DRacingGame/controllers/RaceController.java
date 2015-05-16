package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.RaceListener;
import edu.chl._2DRacingGame.models.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Serves as a basis for all race types.
 *
 * @author Daniel Sunnerberg
 *
 * TODO remove Gdx.app.thread-ish?
 */
public abstract class RaceController implements RaceListener, Disposable {

    private final Player player;
    private GameMap map;
    private GameMode mode;

    private GameScreen screen;
    private GameWorld world;

    protected final GameController gameController;

    public RaceController(GameController gameController) {
        this.gameController = gameController;

        player = new Player();
        player.setIsControlledByClient(true);
    }

    /**
     * Sets the specified map and mode and builds up logic surrounding it, such as checkpoint, screens etc.
     *
     * @param map
     * @param mode
     */
    protected void setRaceProperties(GameMap map, GameMode mode) {
        this.map = map;
        this.mode = mode;

        world = new GameWorld(map);
        CheckpointController checkpointController = new CheckpointController(mode, world.getCheckpoints());
        world.getb2World().setContactListener(new ContactController((checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(checkpoint);
            } else {
                checkpointController.invalidPassing(checkpoint);
            }
        }));

        screen = new GameScreen(world, mode);
    }

    protected Player getPlayer() {
        return player;
    }

    protected GameMap getMap() {
        return map;
    }

    protected GameMode getMode() {
        return mode;
    }

    public GameScreen getScreen() {
        return screen;
    }

    /**
     * Gives away the control to the controller, which starts a series of actions (such as selection of map etc)
     * which eventually leads to the start of the race.
     */
    public abstract void setUp();

    /**
     * Restarts the race.
     */
    public abstract void restartRace();


    /**
     * Disposes all resources for the actual controller.
     * All subclasses should make sure to properly implement this method as needed.
     */
    @Override
    public void dispose() {
        screen.dispose();
        world.dispose();
    }

    public GameWorld getWorld() {
        return world;
    }

}
