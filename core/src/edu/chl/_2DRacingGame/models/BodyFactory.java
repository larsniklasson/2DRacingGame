package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.physics.box2d.*;

/**
 * @author Daniel Sunnerberg
 */
public class BodyFactory {

    public static Body createStaticBody(World world, Shape shape, boolean isSensor) {
        return createStaticBody(world, shape, isSensor, null);
    }

    public static Body createStaticBody(World world, Shape shape, boolean isSensor, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isSensor;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setUserData(userData);

        return body;
    }

}
