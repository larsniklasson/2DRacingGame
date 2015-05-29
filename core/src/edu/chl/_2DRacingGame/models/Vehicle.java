package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;


/**
 * Model for a simple vehicle in a 2D-world.
 * @author Lars Niklasson
 */
public abstract class Vehicle implements Actable {


    private ISteeringSystem steeringSystem;

    /**
     *
     * @return The current position of the vehicle.
     */
    public abstract Vector2 getPosition();

    /**
     *
     * @return The current direction in which the vehicle is facing.
     */
    public abstract float getDirection();

    /**
     *
     * @return The current linear velocity of the vehicle.
     */
    public abstract Vector2 getLinearVelocity();

    /**
     *
     * @return The current angular velocity of the vehicle.
     */
    public abstract float getAngularVelocity();

    /**
     * Calls the vehicle's current SteeringSystem and tells it to update
     * the position, direction and velocities of the vehicle based on the time elapsed since last update.
     *
     * @param delta Time elapsed since last update
     */
    public void update(float delta){
        if(steeringSystem == null)return;
        steeringSystem.update(delta);
    }

    /**
     * Places the vehicle at a specified point, facing a specified direction
     *
     * @param position The position at which the vehicle is placed
     * @param direction The direction the vehicle will be facing
     */
    public abstract void place(Vector2 position, float direction);

    /**
     * @param steeringSystem The new SteeringSystem
     */
    public void setSteeringSystem(ISteeringSystem steeringSystem){
        this.steeringSystem = steeringSystem;
    }

    /**
     * @return whether the vehicle is controlled by our clients player
     */
    public boolean isControlledByClientPlayer() {
        return steeringSystem != null && steeringSystem.isControlledByPlayer();
    }

}
