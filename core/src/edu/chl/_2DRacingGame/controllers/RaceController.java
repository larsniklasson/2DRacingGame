package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.RaceListener;
import edu.chl._2DRacingGame.map.Checkpoint;
import edu.chl._2DRacingGame.map.GameMap;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.screens.GameScreen;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Serves as a controller-basis for the different race types.
 *
 * @author Daniel Sunnerberg
 */
public abstract class RaceController implements RaceListener, MainMenuDisplayer, Disposable, GameScreenListener {

    private final Player player;
    private GameMap map;
    private GameMode mode;

    private GameScreen screen;
    private GameWorld world;

    protected final GameController gameController;

    public RaceController(GameController gameController) {
        this.gameController = gameController;
        player = new Player();
    }

    /**
     * Sets the specified map and mode and builds up logic surrounding it, such as checkpoint, screens etc.
     *
     * @param map map which the race will run on
     * @param mode mode which will determine when the race is complete etc.
     */
    protected void setRaceProperties(GameMap map, GameMode mode) {
        this.map = map;
        this.mode = mode;

        world = new GameWorld(map);
        List<Checkpoint> checkpoints = new ArrayList<>(map.getCheckpoints().keySet());
        CheckpointController checkpointController = new CheckpointController(mode, checkpoints);
        world.getb2World().setContactListener(new ContactController((checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(checkpoint);
            } else {
                checkpointController.invalidPassing(checkpoint);
            }
        }));

        screen = new GameScreen(world, this);
        screen.addScreenTexts(mode.getScreenTexts());
    }

    /**
     * @return our clients player
     */
    protected Player getPlayer() {
        return player;
    }

    /**
     * @return the map which the race is running, or null if none is set yet
     */
    protected GameMap getMap() {
        return map;
    }

    /**
     * @return the mode which the race is running, or null if none is set yet
     */
    protected GameMode getMode() {
        return mode;
    }

    /**
     * @return the game's screen
     */
    public GameScreen getScreen() {
        return screen;
    }

    /**
     * @return the game world which contains our different game objects
     */
    public GameWorld getWorld() {
        return world;
    }


    /**
     * Gives away the control to the controller, which starts a series of actions (such as selection of map etc)
     * which eventually leads to the start of the race.
     */
    public abstract void setUp();

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayMainMenu() {
        gameController.displayStartMenu();
    }

    /**
     * Exits the game.
     */
    public void exitGame() {
        gameController.exitGame();
    }

    /**
     * Disposes all resources for the actual controller.
     * All subclasses should make sure to properly implement this method as needed.
     */
    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }
        if (world != null) {
            world.dispose();
        }
    }

    @Override
    public void resume() {
        mode.resume();
    }

    @Override
    public void pause() {
        mode.pause();
    }
}
