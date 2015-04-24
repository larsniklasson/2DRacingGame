package edu.chl._2DRacingGame;

/**
 * Created by Lasse on 2015-04-24.
 */
public class Dirt implements GroundMaterial{
    @Override
    public float getDrift() {
        return 0.6f;
    }

    @Override
    public float getSpeed() {
        return 0.5f;
    }

    @Override
    public float getDrag() {
        return 3f;
    }
}
