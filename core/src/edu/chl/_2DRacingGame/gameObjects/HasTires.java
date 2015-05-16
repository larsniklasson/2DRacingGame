package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public interface HasTires {

    List<Tire> getTires();
    List<Vector2> getTirePositions();
}
