package edu.chl._2DRacingGame.gameObjects;

import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * Created by Lars Niklasson on 2015-05-19.
 */
public interface Trackable {

    void addGroundMaterial(GroundMaterial gm);
    void removeGroundMaterial(GroundMaterial gm);

}
