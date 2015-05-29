package edu.chl._2DRacingGame.vehicle.steering;


import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.models.ISteeringSystem;

/**
 * SteeringSystem controlled by AI using LibGDX's AI-library. Designed for the WheeledVehicle class.
 *
 *
 * IMPORTANT NOTE:
 * Most code copied from LibGDX's AI-tests. See https://github.com/libgdx/gdx-ai/tree/master/tests/src/com/badlogic/gdx/ai/tests
 * File in particular: https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/steer/box2d/Box2dSteeringEntity.java
 * Most credit goes to davebaol
 *
 * @author davebaol, Lars Niklasson
 */
public abstract class AISteeringSystem implements ISteeringSystem {

    private final AISteeringEntity steeringEntity;

    private SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());


    public AISteeringSystem(AISteeringEntity steeringEntity){
        this.steeringEntity = steeringEntity;
    }

    /**
     * Calculates the steering-acceleration based on the current steering-behaviour and applies it
     * to the steering-entity.
     *
     * @param deltaTime The time elapsed since last update
     */
    @Override
    public void update (float deltaTime) {

        if (steeringBehavior != null) {
            // Calculate steering acceleration

            steeringBehavior.calculateSteering(steeringOutput);


            // Apply steering acceleration
            steeringEntity.applySteering(steeringOutput, deltaTime);
        }


    }

    @Override
    public boolean isControlledByPlayer() {
        return false;
    }


    /**
     * Sets the steering-behavior (See LibGDX's AI-library) used by the system.
     * @param steeringBehavior The steering-behavior the system will use
     */
    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }


}
