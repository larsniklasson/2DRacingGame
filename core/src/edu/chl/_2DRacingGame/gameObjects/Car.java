package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars Niklasson on 2015-03-31.
 */
public class Car {
    private List<Tire> tires;
    public Body body;

    private RevoluteJoint frontLeft, frontRight;


    public int key = 0;
    public int turn = 0;


    //magic numbers
    private float scale = 0.4f* GameWorld.PIXELS_PER_METER;
    private float tireWidth = 0.5f *2 / scale;
    private float tireHeight = 1.25f *2 / scale;


    private float driveForceFront = 2f;
    private float driveForceBack = 1f;

    private float maxImpulseFront = 0.05f;
    private float maxImpulseBack = 0.05f;

    private float maxAngle = 50f;

    public Car(World world){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);


        Vector2[] vertices = new Vector2[8];

        vertices[0] = new Vector2( 1.5f/scale,   0);
        vertices[1] = new Vector2(3/scale, 2.5f/scale);
        vertices[2] = new Vector2(2.8f/scale, 5.5f/scale);
        vertices[3] = new Vector2(1/scale, 10/scale);
        vertices[4] = new Vector2(-1/scale, 10/scale);
        vertices[5] = new Vector2(-2.8f/scale, 5.5f/scale);
        vertices[6] = new Vector2(-3/scale, 2.5f/scale);
        vertices[7] = new Vector2(-1.5f/scale, 0/scale);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        body.createFixture(shape, 0.1f);

        tires = new ArrayList<>();


        //first tire

        RevoluteJointDef jointDef  = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        Tire tire = new Tire(this, world,tireWidth, tireHeight);
        tire.setCharacteristics(driveForceFront, maxImpulseFront);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(-3f/scale, 8.5f/scale));

        frontLeft = (RevoluteJoint) world.createJoint(jointDef);

        tires.add(tire);

        //second tire

        jointDef  = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        Tire tire2 = new Tire(this, world, tireWidth, tireHeight);

        tire2.setCharacteristics(driveForceFront, maxImpulseFront);
        jointDef.bodyB = tire2.getBody();

        jointDef.localAnchorA.set(new Vector2(3f/scale, 8.5f/scale));

        frontRight = (RevoluteJoint) world.createJoint(jointDef);

        tires.add(tire2);


        //third tire

        jointDef  = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        Tire tire3 = new Tire(this, world, tireWidth, tireHeight);

        tire3.setCharacteristics(driveForceBack, maxImpulseBack);
        jointDef.bodyB = tire3.getBody();

        jointDef.localAnchorA.set(new Vector2(-3f/scale, 0/scale));

        world.createJoint(jointDef);

        tires.add(tire3);

        //fourth tire

        jointDef  = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        Tire tire4 = new Tire(this, world, tireWidth, tireHeight);

        tire4.setCharacteristics(driveForceBack, maxImpulseBack);
        jointDef.bodyB = tire4.getBody();

        jointDef.localAnchorA.set(new Vector2(3f/scale, 0/scale));

        world.createJoint(jointDef);

        tires.add(tire4);




    }


    public void update(){

        //turn wheels

        float lockAngle = MathUtils.degreesToRadians * maxAngle;

        float turnSpeedPerSec = 1000/*160*/ * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnSpeedPerSec/60f;
        float desiredAngle = 0;

        if(turn == Input.Keys.LEFT){
            desiredAngle = lockAngle;
        } else if (turn == Input.Keys.RIGHT){
            desiredAngle = -lockAngle;
        }

        float angleNow = frontLeft.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if(angleToTurn < -turnPerTimeStep){
            angleToTurn = -turnPerTimeStep;
        } else if(angleToTurn > turnPerTimeStep){
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        frontLeft.setLimits(newAngle, newAngle);
        frontRight.setLimits(newAngle, newAngle);



        for(Tire t : tires){

            t.update(key);
        }



    }

    public List<Tire> getTires(){
        return tires;
    }

}
