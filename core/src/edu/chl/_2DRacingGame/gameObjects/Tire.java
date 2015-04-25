package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.GroundMaterial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lasse on 2015-03-30.
 */
public class Tire {

    float maxForwardSpeed = 17;
    float maxBackwardSpeed = -7;
    float maxDriveForce;
    float maxLateralImpulse;

    float dragForceMagnitude = -0.02f;


    public float newImp;
    public float newForwardSpeed;
    public float newDrag;
    public float newBackwardSpeed;

    private Body body;
    public List<GroundMaterial> grounds = new ArrayList<GroundMaterial>();





    public Tire(World world, float width, float height){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        body.createFixture(shape, 1);
        body.setUserData(this);


    }

    void setCharacteristics(float maxDriveForce, float maxLateralImpulse){
        this.maxDriveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;
    }


    Vector2 getLateralVelocity(){
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
        Vector2 copy = new Vector2(currentRightNormal.x, currentRightNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }

    void updateFriction(){
        Vector2 impulse = getLateralVelocity().cpy().scl(body.getMass() * -1);



        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));


        float currentSpeed = getForwardVelocity().dot(currentForwardNormal);





        if(impulse.len() > newImp){
            impulse.scl(newImp / impulse.len());
        }

        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        //body.applyAngularImpulse(0f * body.getInertia() * -1 * body.getAngularVelocity(), true);

        currentForwardNormal = getForwardVelocity();


        body.applyForceToCenter(currentForwardNormal.scl(dragForceMagnitude), true);



    }

    void updateDrive(int key){

        float desiredSpeed = 0;
        if(key == Input.Keys.UP){
            desiredSpeed = newForwardSpeed;
        } else if(key == Input.Keys.DOWN){
            desiredSpeed = newBackwardSpeed;
        } else {
            return;
        }

        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));




        float currentSpeed = getForwardVelocity().dot(currentForwardNormal);




        float force = 0;

        if(desiredSpeed > currentSpeed){
            force = maxDriveForce;
        } else if (desiredSpeed < currentSpeed){
            force = -maxDriveForce;
        } else {
            return;
        }


        body.applyForceToCenter(currentForwardNormal.cpy().scl(force), true);

    }


    public void update(int key){


        if(grounds.isEmpty()){

            newImp = maxLateralImpulse;
            newDrag = dragForceMagnitude;
            newForwardSpeed = maxForwardSpeed;
            newBackwardSpeed = maxBackwardSpeed;
        } else {
            GroundMaterial gm = grounds.get(grounds.size()-1);
            newImp = gm.getDrift() * maxLateralImpulse;

            newForwardSpeed = gm.getSpeedFactor() * maxForwardSpeed;
            newDrag = gm.getDrag() * dragForceMagnitude;
            newBackwardSpeed = gm.getSpeedFactor() * maxBackwardSpeed;
        }

        updateFriction();
        updateDrive(key);
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
