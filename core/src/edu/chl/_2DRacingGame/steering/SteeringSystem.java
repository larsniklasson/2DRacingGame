package edu.chl._2DRacingGame.steering;

import edu.chl._2DRacingGame.gameObjects.Vehicle;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class SteeringSystem<T extends Vehicle>{ //TODO Cyclic dependencies
    protected T vehicle;

    public SteeringSystem(T vehicle){
        this.vehicle = vehicle;
    }

    public abstract void update(float delta);

}
