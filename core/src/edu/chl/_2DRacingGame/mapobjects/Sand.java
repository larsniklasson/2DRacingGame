package edu.chl._2DRacingGame.mapobjects;

import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * GroundMaterial for Sand. Like a mild version of dirt.
 * @author Lars Niklasson
 */
public class Sand implements GroundMaterial {
    @Override
    public float getDrift() {
        return 0.8f;
    }

    @Override
    public float getSpeedFactor() {
        return 0.7f;
    }

    @Override
    public float getDrag() {
        return 2f;
    }
}
