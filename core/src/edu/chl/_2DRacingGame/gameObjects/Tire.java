package edu.chl._2DRacingGame.gameObjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.mapobjects.GroundMaterial;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a wheel or a tire (they're the same thing). Used by steering-systems to control a vehicle, particularly TireSteeringSystem.
 * Holds information such as max-speed, driveforce and friction, these values should be set by the vehicle using the tire.
 *
 * @author Lars Niklasson
 */
public class Tire implements Drawable, Trackable {
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

    private final float density;
    private final float width;
    private final float height;

    private final World world;

    private Body body;

    private final List<GroundMaterial> grounds = new ArrayList<>();

    /**
     * Creates a tire (a rectangle) in the specified Box2D-world with the specified width, height and density
     *
     * IMPORTANT NOTE: Calling setCharacteristics is essential and should be done after the object is created.
     *
     * @param world The Box2D-world the tire will be created in
     * @param width The width of the tire.
     * @param height The height of the tire.
     * @param density The density of the tire.
     */
    public Tire(World world, float width, float height, float density) {

        this.world = world;
        this.width = width;
        this.height = height;
        this.density = density;


        createBody();


    }


    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        //shape is just a rectangle
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, density);
        body.setUserData(this);

    }

    /**
     * Sets the characteristics of this tire. Should be called when creating the tire. Usually the maximum speeds
     * should be the same in all tires of the vehicle.
     *
     * @param maxDriveForce The maximum drive-force of this tire.
     * @param maxLateralImpulse The maximum amount of side-ways velocity that can be cancelled out. A lower value will result in more skidding,
     *                          a higher value will make the vehicle seem like it's on rails.
     * @param maxForwardSpeed The maximum forward speed of this tire.
     * @param maxBackwardSpeed The maximum backward speed of this tire.
     * @param roadFrictionBackwardsCoefficient The amount of friction in a backwards direction caused by the road. This makes the vehicle come to a stop on its own.
     */
    public void setCharacteristics(float maxDriveForce, float maxLateralImpulse, float maxForwardSpeed, float maxBackwardSpeed, float roadFrictionBackwardsCoefficient) {
        this.driveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;
        this.maxForwardSpeed = maxForwardSpeed;
        this.maxBackwardSpeed = maxBackwardSpeed;
        this.backwardsFriction = roadFrictionBackwardsCoefficient;
    }


    /**
     * Update the tire-characteristics based on what kind of groundmaterial the tire is touching at the moment.
     */
    public void updateValues(){
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
    }



    @Override
    public void addGroundMaterial(GroundMaterial gm) {
        grounds.add(gm);

    }


    @Override
    public void removeGroundMaterial(GroundMaterial gm) {
        grounds.remove(gm);
    }


    /**
     * Draws the tire's sprite to the screen using a SpriteBatch
     * @param batch The SpriteBatch used to draw the tire's sprite.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if(sprite == null)return;

        SpriteUtils.updateSprite(body, sprite, GameWorld.PIXELS_PER_METER);
        sprite.draw(batch);
    }

    /**
     *
     * @return A copy of this tire.
     */
    public Tire cpy(){
        Tire t = new Tire(world, width, height, density);
        t.setCharacteristics(driveForce, maxLateralImpulse, maxForwardSpeed, maxBackwardSpeed, backwardsFriction);
        t.setSprite(new Sprite(sprite));
        return t;
    }

    /**
     *
     * @return The Box2D-body of this tire.
     */
    public Body getBody() {
        return body;
    }

    /**
     *
     * @return The sprite of this tire.
     */
    public Sprite getSprite(){
        return sprite;
    }

    /**
     *
     * @param sprite The sprite used to draw this tire.
     */
    public void setSprite(Sprite sprite){
        this.sprite = sprite;

    }


    /**
     *
     * @return The drive-force of this tire
     */
    public float getDriveForce(){
        return driveForce;
    }

    /**
     *
     * @param driveForce The drive-force this tire will get.
     */
    public void setDriveForce(float driveForce) {
        this.driveForce = driveForce;
    }


    /**
     *
     * @param maxLateralImpulse The maximum amount of side-ways velocity that can be cancelled out. A lower value will result in more skidding,
     *                          a higher value will make the vehicle seem like it's on rails.
     */
    public void setMaxLateralImpulse(float maxLateralImpulse) {
        this.maxLateralImpulse = maxLateralImpulse;
    }

    /**
     *
     * @param backwardsFriction The amount of friction in a backwards direction caused by the road.
     */
    public void setBackwardsFriction(float backwardsFriction) {
        this.backwardsFriction = backwardsFriction;
    }


    /**
     *
     * @return The CURRENT maximum amount of sideways velocity to be cancelled out.
     */
    public float getCurrentMaxLateralImpulse() {
        return currentMaxLateralImpulse;
    }

    /**
     *
     * @return The CURRENT maximum forward speed of this tire.
     */
    public float getCurrentMaxForwardSpeed() {
        return currentMaxForwardSpeed;
    }

    /**
     *
     * @return The CURRENT maximum backward speed of this tire.
     */
    public float getCurrentBackwardsFriction() {
        return currentBackwardsFriction;
    }

    /**
     *
     * @return The CURRENT amount of friction in a backwards direction caused by the road.
     */
    public float getCurrentMaxBackwardSpeed() {
        return currentMaxBackwardSpeed;
    }


}
