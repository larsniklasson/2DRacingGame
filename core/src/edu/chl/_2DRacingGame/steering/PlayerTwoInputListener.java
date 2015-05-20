package edu.chl._2DRacingGame.steering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Set;
import java.util.TreeSet;

/**
 * Class for polling for Input. Designed for Local multiplayer, where this class is used by player 2.
 *@author Lars Niklasson
 */
public class PlayerTwoInputListener implements SteeringInputListener {
    @Override
    public Set<Key> getInput() {
        Set<Key> keysPressed = new TreeSet<>();

        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            keysPressed.add(Key.Up);
        } else if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            keysPressed.add(Key.Down);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            keysPressed.add(Key.Left);
        } else if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            keysPressed.add(Key.Right);
        }

        return keysPressed;
    }
}
