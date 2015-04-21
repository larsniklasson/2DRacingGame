package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lasse on 2015-03-30.
 */
public class Tire {

    float maxForwardSpeed = 17;
    float maxBackwardSpeed = -7;
    float maxDriveForce;
    float maxLateralImpulse;

    Body body;




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

    void updateFriction(float angle){
        Vector2 impulse = getLateralVelocity().cpy().scl(body.getMass() * -1);
        //System.out.println("impulse length " + impulse.len());


        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));


        float currentSpeed = getForwardVelocity().dot(currentForwardNormal);

        float newImp = maxLateralImpulse;
        //System.out.println("hastighet " + getForwardVelocity().len());
        //System.out.println("newImp = " + newImp);
        //System.out.println("oldIMp = " + maxLateralImpulse);

        //System.out.println("maybe " +  currentSpeed *(1+angle));


        if(impulse.len() > newImp){
            impulse.scl(newImp / impulse.len());
        }

        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        //body.applyAngularImpulse(0f * body.getInertia() * -1 * body.getAngularVelocity(), true);

        currentForwardNormal = getForwardVelocity();

        float dragForceMagnitude = -0.02f;
        body.applyForceToCenter(currentForwardNormal.scl(dragForceMagnitude), true);



    }

    void updateDrive(int key){

        float desiredSpeed = 0;
        if(key == Input.Keys.UP){
            desiredSpeed = maxForwardSpeed;
        } else if(key == Input.Keys.DOWN){
            desiredSpeed = maxBackwardSpeed;
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



    private Vector2 getForwardVelocity() {
        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0,1));
        Vector2 copy = new Vector2(currentForwardNormal.x, currentForwardNormal.y);
        return copy.scl(copy.dot(body.getLinearVelocity()));
    }
}
