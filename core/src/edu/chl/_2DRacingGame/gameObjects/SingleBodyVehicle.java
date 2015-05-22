package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Model for a vehicle that uses only one instance of a Box2D-Body object
 * as the main source for getting info from the vehicle, such as position, velocity, etc.
 *
 * @author Lars Niklasson
 */
public abstract class SingleBodyVehicle extends Vehicle {
    protected Body body;
    protected final World world;

    /**
     * Creates a vehicle set in the specified Box2D-world.
     * IMPORTANT NOTE: Calling createBody() is necessary to have a functioning vehicle when subclassing.
     *
     * @param world the Box2D-world which the vehicle will be created in.
     */
    public SingleBodyVehicle(World world){
        this.world = world;
    }

    @Override
    public Vector2 getPosition() {
        return body.getTransform().getPosition();
    }

    @Override
    public float getDirection() {
        return body.getTransform().getRotation();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public void place(Vector2 position, float direction) {
        body.setTransform(position, direction);
    }

    /**
     *
     * @return The Body-object of this instance
     */
    public Body getBody(){
        return body;
    }


    /**
     * Creates a dynamic body with the specified shape an density
     *
     * @param shape The shape the vehicle-body will have
     * @param density The density the vehicle-body will have
     */
    protected void createBody(Shape shape, float density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(shape, density);
    }
}
