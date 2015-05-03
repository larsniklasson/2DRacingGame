package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.GroundMaterial;
import edu.chl._2DRacingGame.helperClasses.InputManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-03-30.
 */
public class Tire {

    //more magic numbers
    private float maxForwardSpeed;
    private float maxBackwardSpeed;
    private float maxDriveForce;
    private float maxLateralImpulse;

    private float roadFrictionBackwardsCoefficient;

    private  float newImp;
    private  float newForwardSpeed;
    private  float newDrag;
    private  float newBackwardSpeed;

    private Body body;
    public List<GroundMaterial> grounds = new ArrayList<>();

    public Tire(World world, float width, float height){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        //shape is just a rectangle
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        body.createFixture(shape, 1);
        body.setUserData(this);
    }

    void setCharacteristics(float maxDriveForce, float maxLateralImpulse, float maxForwardSpeed, float maxBackwardSpeed, float roadFrictionBackwardsCoefficient){
        this.maxDriveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;
        this.maxForwardSpeed = maxForwardSpeed;
        this.maxBackwardSpeed = maxBackwardSpeed;
        this.roadFrictionBackwardsCoefficient = roadFrictionBackwardsCoefficient;
    }


    //sideways velocity
    private Vector2 getLateralVelocity(){
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
        Vector2 copy = new Vector2(currentRightNormal.x, currentRightNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }



    private void updateFriction(){
        reduceSideWaysVelocity();


        applyRoadFriction();



    }

    private void applyRoadFriction() {
        Vector2 currentForwardNormal = getForwardVelocity();

        body.applyForceToCenter(currentForwardNormal.scl(newDrag), true);
    }

    private void reduceSideWaysVelocity() {
        Vector2 impulse = getLateralVelocity().cpy().scl(body.getMass() * -1);

        //the amount of sideways velocity cancelled cant exceed a certain maximum value - creating the skidding/sliding effect

        if(impulse.len() > newImp){
            impulse.scl(newImp / impulse.len());
        }

        //cancel out sideways velocity
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }



    private void updateDrive(Set<InputManager.PressedKey> keys){

        float desiredSpeed;
        if (keys.contains(InputManager.PressedKey.Up)) {

            desiredSpeed = newForwardSpeed;
        } else if (keys.contains(InputManager.PressedKey.Down)) {

            desiredSpeed = newBackwardSpeed;
        } else {
            return;
        }



        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));

        float currentSpeed = getForwardVelocity().dot(currentForwardNormal);


        float force;

        //accelerate up to a certaint point. (max speed)

        if(desiredSpeed > currentSpeed){
            force = maxDriveForce;
        } else if (desiredSpeed < currentSpeed){
            force = -maxDriveForce;
        } else {
            return;
        }


        body.applyForceToCenter(currentForwardNormal.cpy().scl(force), true);

    }


    public void update(Set<InputManager.PressedKey> keys){



        if(grounds.isEmpty()){

            newImp = maxLateralImpulse;
            newDrag = roadFrictionBackwardsCoefficient;
            newForwardSpeed = maxForwardSpeed;
            newBackwardSpeed = maxBackwardSpeed;
        } else {
            GroundMaterial gm = grounds.get(grounds.size()-1);
            newImp = gm.getDrift() * maxLateralImpulse;

            newForwardSpeed = gm.getSpeedFactor() * maxForwardSpeed;
            newDrag = gm.getDrag() * roadFrictionBackwardsCoefficient;
            newBackwardSpeed = gm.getSpeedFactor() * maxBackwardSpeed;
        }

        updateFriction();
        updateDrive(keys);
    }


    private Vector2 getForwardVelocity() {
        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0,1));
        Vector2 copy = new Vector2(currentForwardNormal.x, currentForwardNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }

    public Body getBody(){
        return body;
    }
}
