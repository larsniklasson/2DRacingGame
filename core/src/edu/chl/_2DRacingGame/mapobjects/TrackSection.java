package edu.chl._2DRacingGame.mapobjects;


import com.badlogic.gdx.physics.box2d.*;

/**
 * Class representing an area on the ground.
 *
 *@author Lars Niklasson
 */
public class TrackSection {


    private final GroundMaterial groundMaterial;

    /**
     * Creates a new TrackSection in the specified Box2D-world, with the specified Box2d-shape and groundmaterial
     *
     * @param world The Box2D-world the TrackSection will be created in
     * @param shape The Box2D-shape the TrackSection will get
     * @param groundMaterial The GroundMaterial the TrackSection will get.
     */
    public TrackSection(World world, Shape shape, GroundMaterial groundMaterial){
        this.groundMaterial = groundMaterial;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);
        body.setUserData(this);




        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;



        body.createFixture(fixtureDef);
    }

    /**
     *
     * @return The GroundMaterial of this TrackSection.
     */
    public GroundMaterial getGroundMaterial(){
        return groundMaterial;
    }

}
