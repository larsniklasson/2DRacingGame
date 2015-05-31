package edu.chl._2DRacingGame.map.objects;

/**
 * Interface for a ground-material.
 * Used to control how different grounds affect the vehicles.
 *
 *@author Lars Niklasson
 */
public interface GroundMaterial {
    /**
     *
     * @return The drift-coefficient of this ground-material
     */
    float getDrift();

    /**
     *
     * @return The speed-coefficient of this ground-material
     */
    float getSpeedFactor();

    /**
     *
     * @return The draw-coefficient of this ground-material
     */
    float getDrag();


}
