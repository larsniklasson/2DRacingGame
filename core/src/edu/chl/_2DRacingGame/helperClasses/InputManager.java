package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;


import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class InputManager {

    public enum PressedKey{
        Up, Down, Left, Right
    }

    public static Set<PressedKey> pollForInput() {
        Set<PressedKey> keysPressed = new TreeSet<>();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            keysPressed.add(PressedKey.Up);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            keysPressed.add(PressedKey.Down);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            keysPressed.add(PressedKey.Left);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            keysPressed.add(PressedKey.Right);
        }

        return keysPressed;

    }


}
