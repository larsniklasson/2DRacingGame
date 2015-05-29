package edu.chl._2DRacingGame.vehicle.steering;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;

/**
 * Interface for an AI-controlled entity
 * @author Lars Niklasson
 */
public interface AISteeringEntity extends Steerable<Vector2>{

    /**
     * Applies the calculated steering-acceleration to the entity.
     *
     * @param steeringAcceleration The steering-acceleration to be applied.
     * @param deltaTime The time the velocites and forces should be scaled by. Typically the time elapsed since last calling this method.
     */
    void applySteering(SteeringAcceleration<Vector2> steeringAcceleration, float deltaTime);


}
