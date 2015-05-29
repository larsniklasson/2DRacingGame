package edu.chl._2DRacingGame.gameObjects.steering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Set;
import java.util.TreeSet;

/**
 * Class for polling for Input. Designed for Local multiplayer, where this class is used by player 1.
 *@author Lars Niklasson
 */
public class PlayerOneInputListener implements SteeringInputListener {


    @Override
    public Set<Key> getInput() {

        Set<Key> keysPressed = new TreeSet<>();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            keysPressed.add(Key.Up);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            keysPressed.add(Key.Down);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            keysPressed.add(Key.Left);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            keysPressed.add(Key.Right);
        }

        return keysPressed;


    }
}
