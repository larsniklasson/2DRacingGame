package edu.chl._2DRacingGame.mapobjects;

import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * Created by Lars Niklasson on 2015-05-13.
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
