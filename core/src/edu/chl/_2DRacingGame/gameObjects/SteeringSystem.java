package edu.chl._2DRacingGame.gameObjects;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class SteeringSystem{
    Vehicle v;

    public SteeringSystem(Vehicle v){
        this.v = v;
    }

    abstract void update(float delta);

}
