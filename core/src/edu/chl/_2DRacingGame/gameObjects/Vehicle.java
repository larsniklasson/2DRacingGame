package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class Vehicle{

    //TODO vector2 fix (no libgdx code here)

    private SteeringSystem steeringSystem;
    public abstract Vector2 getPosition();

    //not used ATM but AI-system will make use of these later
    public abstract float getDirection();
    public abstract Vector2 getLinearVelocity();
    public abstract float getAngularVelocity();

    public void update(float delta){
        steeringSystem.update(delta);
    }

    public abstract void place(Vector2 position, float direction);

    public void setSteeringSystem(SteeringSystem steeringSystem){
        this.steeringSystem = steeringSystem;
    }

}
