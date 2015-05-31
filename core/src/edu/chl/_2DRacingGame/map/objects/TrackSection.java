package edu.chl._2DRacingGame.map.objects;


import com.badlogic.gdx.physics.box2d.*;

/**
 * Class representing an area on the ground.
 *
 *@author Lars Niklasson
 */
public class TrackSection {


    private final GroundMaterial groundMaterial;
    private final Shape shape;

    /**
     * Creates a new TrackSection with the specified Box2d-shape and ground material
     *
     * @param shape The Box2D-shape the TrackSection will get
     * @param groundMaterial The GroundMaterial the TrackSection will get.
     */
    public TrackSection(Shape shape, GroundMaterial groundMaterial){
        this.groundMaterial = groundMaterial;
        this.shape = shape;
    }

    /**
     * @return The GroundMaterial of this TrackSection.
     */
    public GroundMaterial getGroundMaterial(){
        return groundMaterial;
    }

    /**
     * @return the track section's shape
     */
    public Shape getShape() {
        return shape;
    }
}
