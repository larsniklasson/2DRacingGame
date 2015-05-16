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
 * Created by Lars Niklasson on 2015-03-30.
 */
public class Tire implements Drawable {
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

    private float density;
    private float width;
    private float height;

    private World world;

    private Body body;

    public final List<GroundMaterial> grounds = new ArrayList<>();

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

    public void setCharacteristics(float maxDriveForce, float maxLateralImpulse, float maxForwardSpeed, float maxBackwardSpeed, float roadFrictionBackwardsCoefficient) {
        this.driveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;
        this.maxForwardSpeed = maxForwardSpeed;
        this.maxBackwardSpeed = maxBackwardSpeed;
        this.backwardsFriction = roadFrictionBackwardsCoefficient;
    }





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



    public void addGroundMaterial(GroundMaterial gm) {
        grounds.add(gm);  //TODO maybe updatevalues in here

    }

    public void removeGroundMaterial(GroundMaterial gm) {
        grounds.remove(gm);
    }



    @Override
    public void draw(SpriteBatch batch) {
        if(sprite == null)return;

        SpriteUtils.updateSprite(body, sprite, GameWorld.PIXELS_PER_METER);
        sprite.draw(batch);
    }


    public Tire cpy(){
        Tire t = new Tire(world, width, height, density);
        t.setCharacteristics(driveForce, maxLateralImpulse, maxForwardSpeed, maxBackwardSpeed, backwardsFriction);
        t.setSprite(new Sprite(sprite));
        return t;
    }

    public Body getBody() {
        return body;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;

    }



    public float getDriveForce(){
        return driveForce;
    }

    public void setDriveForce(float driveForce) {
        this.driveForce = driveForce;
    }

    public void setMaxForwardSpeed(float maxForwardSpeed) {
        this.maxForwardSpeed = maxForwardSpeed;
    }

    public void setMaxBackwardSpeed(float maxBackwardSpeed) {
        this.maxBackwardSpeed = maxBackwardSpeed;
    }

    public void setMaxLateralImpulse(float maxLateralImpulse) {
        this.maxLateralImpulse = maxLateralImpulse;
    }

    public void setBackwardsFriction(float backwardsFriction) {
        this.backwardsFriction = backwardsFriction;
    }




    public float getCurrentMaxLateralImpulse() {
        return currentMaxLateralImpulse;
    }

    public float getCurrentMaxForwardSpeed() {
        return currentMaxForwardSpeed;
    }

    public float getCurrentBackwardsFriction() {
        return currentBackwardsFriction;
    }

    public float getCurrentMaxBackwardSpeed() {
        return currentMaxBackwardSpeed;
    }


}
