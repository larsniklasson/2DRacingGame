package edu.chl._2DRacingGame.game;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Helps create LibGDX bodies.
 *
 * @author Daniel Sunnerberg
 */
class BodyFactory {

    /**
     * Creates a new static body in the game with the specified properties.
     *
     * @param world game which the body should be placed in
     * @param shape the bodies shape
     * @param isSensor whether the body is a sensor or not
     * @return the created body
     */
    public static Body createStaticBody(World world, Shape shape, boolean isSensor) {
        return createStaticBody(world, shape, isSensor, null);
    }

    /**
     * Creates a new static body in the game with the specified properties.
     *
     * @param world game which the body should be placed in
     * @param shape the bodies shape
     * @param isSensor whether the body is a sensor or not
     * @param userData the bodies attached user data
     * @return the created body
     */
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
