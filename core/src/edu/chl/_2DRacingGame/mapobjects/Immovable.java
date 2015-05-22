package edu.chl._2DRacingGame.mapobjects;


import com.badlogic.gdx.physics.box2d.*;

/**
 * An Immovable (static) gameobject
 * @author Lars Niklasson
 */
public class Immovable {
    //basically an immovable body

    /**
     * Creates an immovable object in the specified Box2D-world with the specified Box2D-shape.
     * @param world The Box2D-world the immovable will be created in.
     * @param shape The Box2D-shape the immovable will get.
     */
    public Immovable(World world, Shape shape){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        Body body = world.createBody(bodyDef);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;


        fixtureDef.isSensor = false;

        body.createFixture(fixtureDef);



    }
}
