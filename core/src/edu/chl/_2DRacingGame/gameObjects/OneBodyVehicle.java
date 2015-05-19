package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class OneBodyVehicle extends Vehicle {
    protected Body body;
    protected final World world;

    public OneBodyVehicle(World world){
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

    public Body getBody(){
        return body;
    }



    protected void createBody(Shape shape, float density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(shape, density);
    }
}
