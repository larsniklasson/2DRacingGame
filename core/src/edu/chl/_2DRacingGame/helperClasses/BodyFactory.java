package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.physics.box2d.*;

/**
 * @author Daniel Sunnerberg
 */
class BodyFactory {

    public static Body createStaticBody(World world, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        return body;
    }

}
