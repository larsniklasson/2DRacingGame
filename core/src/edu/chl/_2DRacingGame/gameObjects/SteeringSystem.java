package edu.chl._2DRacingGame.gameObjects;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class SteeringSystem<T extends Vehicle>{
    T vehicle;

    public SteeringSystem(T vehicle){
        this.vehicle = vehicle;
    }

    abstract void update(float delta);

}
