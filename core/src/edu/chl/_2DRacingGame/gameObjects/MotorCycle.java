package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
 * Created by Lars Niklasson on 2015-05-08.
 */
public class MotorCycle implements Vehicle{

    private Sprite sprite;
    private Sprite tireSprite;
    private Sprite tireSprite2;

    private List<Tire> tires;
    private Body body;
    private final World world;

    private RevoluteJoint frontJoint;


    //magic numbers
    private static final float SCALE = 0.4f * GameWorld.PIXELS_PER_METER;
    private static final float TIRE_WIDTH = 0.75f * 2 / SCALE;
    private static final float TIRE_HEIGHT = 1.75f * 2 / SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 1.5f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.07f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.07f;

    private static final float TURN_DEGREES_PER_SECOND = 1000;

    private static final float MAX_ANGLE = 30f;

    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;



    public MotorCycle(World world) {

        Texture texture = new Texture(Gdx.files.internal("mc_body.png"));
        sprite = new Sprite(texture);

        Texture tireTexture = new Texture(Gdx.files.internal("mc_tire.png"));
        tireSprite = new Sprite(tireTexture);

        Texture tireTexture2 = new Texture(Gdx.files.internal("mc_tire.png"));
        tireSprite2 = new Sprite(tireTexture2);


        this.world = world;

        createBody();

        createAndAttachTires();


    }

    private void updateSprite(){
        sprite.setPosition((body.getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (body.getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
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

        Tire tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        tire.setSprite(tireSprite);

        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(-0f, 10f/GameWorld.PIXELS_PER_METER));

        frontJoint = (RevoluteJoint) world.createJoint(jointDef);

        tires.add(tire);

        //second tire

        tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        tire.setSprite(tireSprite2);

        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(0f, -10f/GameWorld.PIXELS_PER_METER));

        world.createJoint(jointDef);

        tires.add(tire);



    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);




        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8f/GameWorld.PIXELS_PER_METER/2, 24f/GameWorld.PIXELS_PER_METER/2);

        body.createFixture(shape, 0.1f);
    }


    public void update(Set<InputManager.PressedKey> keys) {




        turnWheels(keys);


        for (Tire t : tires) {

            t.update(keys);
        }

        updateSprite();



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

        float angleNow = frontJoint.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        frontJoint.setLimits(newAngle, newAngle);

    }

    public List<Tire> getTires() {
        return tires;
    }

    public Body getBody() {
        return body;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public List<Sprite> getSprites() {
        List<Sprite> list = new ArrayList<>();
        list.add(sprite);
        for(Tire t: tires){
            list.add(t.getSprite());
        }
        return list;

    }

    @Override
    public void place(Vector2 position, float angle) {
        body.setTransform(position, angle);
        for(Tire t : tires){
            t.getBody().setTransform(position,0);
        }
    }


}
