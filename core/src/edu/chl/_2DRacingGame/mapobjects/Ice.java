package edu.chl._2DRacingGame.mapobjects;

/**
 * Created by Lars Niklasson on 2015-04-24.
 */
public class Ice implements GroundMaterial{
    @Override
    public float getDrift() {
        return 0.2f;
    }

    @Override
    public float getSpeedFactor() {
        return 0.9f;
    }

    @Override
    public float getDrag() {
        return 0.5f;
    }
}
