package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.GroundMaterial;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-03-30.
 */
public class Tire {
    private Sprite sprite;

    private float maxForwardSpeed;
    private float maxBackwardSpeed;
    private float driveForce;
    private float maxLateralImpulse;

    private float backwardsFriction;

    private float currentMaxLateralImpulse;
    private float currentMaxForwardSpeed;
    private float currentBackwardsFriction;
    private float currentMaxBackwardSpeed;

    private final Body body;
    public final List<GroundMaterial> grounds = new ArrayList<>();

    public Tire(World world, float width, float height, float density) {
        Texture texture = new Texture(Gdx.files.internal("tire.png"));
        sprite = new Sprite(texture);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        //shape is just a rectangle
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, density);
        body.setUserData(this);
    }

    void setCharacteristics(float maxDriveForce, float maxLateralImpulse, float maxForwardSpeed, float maxBackwardSpeed, float roadFrictionBackwardsCoefficient) {
        this.driveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;
        this.maxForwardSpeed = maxForwardSpeed;
        this.maxBackwardSpeed = maxBackwardSpeed;
        this.backwardsFriction = roadFrictionBackwardsCoefficient;
    }

    private void updateSprite(){
        sprite.setPosition((body.getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (body.getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));

    }


    //sideways velocity
    private Vector2 getLateralVelocity() {
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
        Vector2 copy = new Vector2(currentRightNormal.x, currentRightNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }


    private void updateFriction() {
        reduceSideWaysVelocity();


        applyRoadFriction();


    }

    private void applyRoadFriction() {
        Vector2 currentForwardNormal = getForwardVelocity();

        body.applyForceToCenter(currentForwardNormal.scl(currentBackwardsFriction), true);
    }

    private void reduceSideWaysVelocity() {
        Vector2 impulse = getLateralVelocity().cpy().scl(body.getMass() * -1);

        //the amount of sideways velocity cancelled cant exceed a certain maximum value - creating the skidding/sliding effect

        if (impulse.len() > currentMaxLateralImpulse) {
            impulse.scl(currentMaxLateralImpulse / impulse.len());
        }

        //cancel out sideways velocity
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }


    private void updateDrive(Set<InputManager.PressedKey> keys) {

        float desiredSpeed;
        if (keys.contains(InputManager.PressedKey.Up)) {

            desiredSpeed = currentMaxForwardSpeed;
        } else if (keys.contains(InputManager.PressedKey.Down)) {

            desiredSpeed = currentMaxBackwardSpeed;
        } else {
            return;
        }


        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));

        float currentSpeed = getForwardVelocity().dot(currentForwardNormal);


        float force;

        //accelerate up to a certain point. (max speed)

        if (desiredSpeed > currentSpeed) {
            force = driveForce;
        } else if (desiredSpeed < currentSpeed) {
            force = -driveForce;
        } else {
            return;
        }


        body.applyForceToCenter(currentForwardNormal.cpy().scl(force), true);

    }


    public void update(Set<InputManager.PressedKey> keys) {


        if (grounds.isEmpty()) {

            currentMaxLateralImpulse = maxLateralImpulse;
            currentBackwardsFriction = backwardsFriction;
            currentMaxForwardSpeed = maxForwardSpeed;
            currentMaxBackwardSpeed = maxBackwardSpeed;
        } else {
            GroundMaterial gm = grounds.get(grounds.size() - 1);
            currentMaxLateralImpulse = gm.getDrift() * maxLateralImpulse;

            currentMaxForwardSpeed = gm.getSpeedFactor() * maxForwardSpeed;
            currentBackwardsFriction = gm.getDrag() * backwardsFriction;
            currentMaxBackwardSpeed = gm.getSpeedFactor() * maxBackwardSpeed;
        }

        updateFriction();
        updateDrive(keys);

        updateSprite();
    }

    public void addGroundMaterial(GroundMaterial gm) {
        grounds.add(gm);

    }

    public void removeGroundMaterial(GroundMaterial gm) {
        grounds.remove(gm);
    }


    private Vector2 getForwardVelocity() {
        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));
        Vector2 copy = new Vector2(currentForwardNormal.x, currentForwardNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }

    public Body getBody() {
        return body;
    }
    public Sprite getSprite(){
        return sprite;
    }
}
