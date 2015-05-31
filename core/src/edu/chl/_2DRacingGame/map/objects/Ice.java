package edu.chl._2DRacingGame.map.objects;

/**
 * GroundMaterial for Ice. Easier to slide/drift but less drag. (friction applied in a backwards direction)
 *@author Lars Niklasson
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
