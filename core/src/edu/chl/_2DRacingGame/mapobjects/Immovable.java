package edu.chl._2DRacingGame.mapobjects;


import com.badlogic.gdx.physics.box2d.*;

/**
 * An Immovable (static) gameobject
 * @author Lars Niklasson
 */
public class Immovable {

    private final Shape shape;
    
    /**
     * Creates an immovable object in the specified Box2D-world with the specified Box2D-shape.
     * @param shape The Box2D-shape the immovable will get.
     */
    public Immovable(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }
}
