package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.helperClasses.InputManager;

import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-06.
 */
public interface Vehicle {
    void update(Set<InputManager.PressedKey> keys);
    List<Sprite> getSprites();
    void moveTo(Vector2 position, float angle);
    
}
