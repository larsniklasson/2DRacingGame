package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import edu.chl._2DRacingGame.helperClasses.InputManager;

import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-03-31.
 */
public class Car {
    private List<Tire> tires;
    private Body body;
    private final World world;

    private RevoluteJoint frontLeft, frontRight;


    //magic numbers
    private static final float SCALE = 0.4f * GameWorld.PIXELS_PER_METER;
    private static final float TIRE_WIDTH = 0.5f * 2 / SCALE;
    private static final float TIRE_HEIGHT = 1.25f * 2 / SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 2f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.05f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.05f;

    private static final float TURN_DEGREES_PER_SECOND = 1000;

    private static final float MAX_ANGLE = 50f;

    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;

    public Car(World world) {
        this.world = world;

        createCarBody();

        createAndAttachTires();


    }


    private void createAndAttachTires() {
        tires = new ArrayList<>();


        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        //first tire

        Tire tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT);
        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(-3f / SCALE, 8.5f / SCALE));

        frontLeft = (RevoluteJoint) world.createJoint(jointDef);

        tires.add(tire);

        //second tire

        tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT);

        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(3f / SCALE, 8.5f / SCALE));

        frontRight = (RevoluteJoint) world.createJoint(jointDef);

        tires.add(tire);


        //third tire


        tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT);

        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(-3f / SCALE, 0 / SCALE));

        world.createJoint(jointDef);

        tires.add(tire);

        //fourth tire

        tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT);

        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(3f / SCALE, 0 / SCALE));

        world.createJoint(jointDef);

        tires.add(tire);
    }

    private void createCarBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);


        Vector2[] vertices = new Vector2[8];

        vertices[0] = new Vector2(1.5f / SCALE, 0);
        vertices[1] = new Vector2(3 / SCALE, 2.5f / SCALE);
        vertices[2] = new Vector2(2.8f / SCALE, 5.5f / SCALE);
        vertices[3] = new Vector2(1 / SCALE, 10 / SCALE);
        vertices[4] = new Vector2(-1 / SCALE, 10 / SCALE);
        vertices[5] = new Vector2(-2.8f / SCALE, 5.5f / SCALE);
        vertices[6] = new Vector2(-3 / SCALE, 2.5f / SCALE);
        vertices[7] = new Vector2(-1.5f / SCALE, 0 / SCALE);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        body.createFixture(shape, 0.1f);
    }


    public void update() {

        Set<InputManager.PressedKey> keys = InputManager.pollForInput();


        turnWheels(keys);


        for (Tire t : tires) {

            t.update(keys);
        }


    }

    private void turnWheels(Set<InputManager.PressedKey> keys) {

        float lockAngle = MathUtils.degreesToRadians * MAX_ANGLE;

        float turnRadiansPerSec = TURN_DEGREES_PER_SECOND * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnRadiansPerSec / 60f;
        float desiredAngle = 0;

        if (keys.contains(InputManager.PressedKey.Left)) {
            desiredAngle = lockAngle;

        } else if (keys.contains(InputManager.PressedKey.Right)) {
            desiredAngle = -lockAngle;
        }

        float angleNow = frontLeft.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        frontLeft.setLimits(newAngle, newAngle);
        frontRight.setLimits(newAngle, newAngle);
    }

    public List<Tire> getTires() {
        return tires;
    }

    public Body getBody() {
        return body;
    }

}
