package edu.chl._2DRacingGame.mapobjects;

/**
 *GroundMaterial for Dirt. Slows down the vehicle significantly.
 * @author Lars Niklasson
 */
public class Dirt implements GroundMaterial{
    @Override
    public float getDrift() {
        return 0.6f;
    }

    @Override
    public float getSpeedFactor() {
        return 0.5f;
    }

    @Override
    public float getDrag() {
        return 3f;
    }
}
