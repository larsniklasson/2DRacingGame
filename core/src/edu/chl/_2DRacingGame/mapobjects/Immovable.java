package edu.chl._2DRacingGame.mapobjects;


import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class Immovable {
    //basically an immovable body

    private Body body;

    public Immovable(World world, Shape shape){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        body = world.createBody(bodyDef);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;


        fixtureDef.isSensor = false;

        body.createFixture(fixtureDef);





    }
}
