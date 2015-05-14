package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.helperClasses.VehicleActor;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-08.
 */
public abstract class Vehicle {

    //TODO Vehicle archtecture should be overlooked (finish AI before)

    private float currentFrontWheelAngle = 0;
    private float MP_angleToSetFrontTires = 0;

    private List<Vector2> tirePositions = new ArrayList<>();
    private List<Boolean> isFrontWheelBooleanList = new ArrayList<>();

    private float turnDegreesPerSecond = 10000f;
    private float maxAngle = 50f;

    private List<Tire> tires = new ArrayList<>();
    private List<RevoluteJoint> frontJoints = new ArrayList<>();

    private final VehicleActor actor = new VehicleActor(this);

    private Sprite sprite;
    private Body body;
    private World world;

    public Vehicle(World world){
        this.world = world;
    }

    protected void setTurnDegreesPerSecond(float degrees){
        turnDegreesPerSecond = degrees;
    }

    protected void setMaxAngle(float angle){
        maxAngle = angle;
    }

    protected void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    public void MPspriteUpdate(){
        updateSprite();
        for (int i = 0; i < tires.size(); i++) {
            Tire t = tires.get(i);
            t.updateSprite();
            if(isFrontWheelBooleanList.get(i)){
                t.rotateSprite(t.getBody().getTransform().getRotation() + MP_angleToSetFrontTires);
            }
        }
    }

    public void updateSprite(){
        sprite.setPosition((body.getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (body.getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    protected void attachTire(Tire tire, Vector2 position, boolean frontTire){
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set(position);

        if(frontTire){
            frontJoints.add((RevoluteJoint) world.createJoint(jointDef));

        } else {
            world.createJoint(jointDef);
        }

        tirePositions.add(position);

        tires.add(tire);

        isFrontWheelBooleanList.add(frontTire);
    }

    protected void createBody(Shape shape, float density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(shape, 0.1f);
    }

    public void turnWheels(Set<InputManager.PressedKey> keys) {

        float lockAngle = MathUtils.degreesToRadians * maxAngle;

        float turnRadiansPerSec = turnDegreesPerSecond * MathUtils.degreesToRadians; //instant as it is now
        float turnPerTimeStep = turnRadiansPerSec / 60f;
        float desiredAngle = 0;

        if (keys.contains(InputManager.PressedKey.Left)) {
            desiredAngle = lockAngle;

        } else if (keys.contains(InputManager.PressedKey.Right)) {
            desiredAngle = -lockAngle;
        }

        float angleNow = frontJoints.get(0).getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (angleToTurn < -turnPerTimeStep) {
            angleToTurn = -turnPerTimeStep;
        } else if (angleToTurn > turnPerTimeStep) {
            angleToTurn = turnPerTimeStep;
        }

        float newAngle = angleNow + angleToTurn;

        for(RevoluteJoint rj : frontJoints){
            rj.setLimits(newAngle, newAngle);
        }

        currentFrontWheelAngle = newAngle;

    }

    public void update(Set<InputManager.PressedKey> keys) {
        turnWheels(keys);

        for (Tire t : tires) {
            t.update(keys);
        }

        if(sprite != null){
            updateSprite();
        }

        updateSprite();
    }

    public List<Sprite> getSprites() {
        List<Sprite> list = new ArrayList<>();
        list.add(sprite);
        for(Tire t : tires) {
            list.add(t.getSprite());
        }
        return list;
    }

    public void place(Vector2 position, float angle) {
        body.setTransform(position, angle);
        for(int i = 0; i < tirePositions.size(); i++){
            Tire t = tires.get(i);
            Vector2 pos = tirePositions.get(i);
            t.getBody().setTransform(body.getWorldPoint(pos), angle);
        }
    }

    public Body getBody() {
        return body;
    }

    public VehicleActor getActor() {
        return actor;
    }

    public void resetForces() {
        body.setLinearVelocity(new Vector2(0, 0));
        body.setAngularVelocity(0);

        for (Tire tire : tires) {
            tire.getBody().setLinearVelocity(new Vector2(0, 0));
            tire.getBody().setAngularVelocity(0);
        }
    }

    public List<Tire> getTires(){
        return tires;
    }

    public float getCurrentFrontWheelAngle() {
        return currentFrontWheelAngle;
    }

    public void setMP_angleToSetFrontTires(float MP_angleToSetFrontTires) {
        this.MP_angleToSetFrontTires = MP_angleToSetFrontTires;
    }
}
