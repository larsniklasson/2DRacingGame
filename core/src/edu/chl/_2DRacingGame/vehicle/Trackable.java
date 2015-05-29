package edu.chl._2DRacingGame.vehicle;

import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * Interface for keeping track what groundmaterials an object is currently touching
 *@author Lars Niklasson
 */
public interface Trackable {
    /**
     *
     * @param gm The groundmaterial to be added
     */
    void addGroundMaterial(GroundMaterial gm);

    /**
     *
     * @param gm The groundmaterial to be removed.
     */
    void removeGroundMaterial(GroundMaterial gm);

}
