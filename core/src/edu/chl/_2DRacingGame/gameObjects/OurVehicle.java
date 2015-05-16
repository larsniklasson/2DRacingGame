package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars Niklasson on 2015-05-15.
 */
public abstract class OurVehicle extends OneBodyVehicle implements HasTires, Drawable {
    private Sprite sprite;

    List<Tire> tires = new ArrayList<>();
    List<Vector2> tirePositions = new ArrayList<>();

    List<RevoluteJoint> frontJoints = new ArrayList<>();
    private float maxTurnAngle = 50f;
    private float turnDegreesPerSecond = 1000;



    private List<Boolean> isFrontWheelBooleanList = new ArrayList<>();  //maybe not needed but was used for MP before

    public OurVehicle(World world) {
        super(world);


    }



    @Override
    public List<Tire> getTires() {
        return tires;
    }

    @Override
    public List<Vector2> getTirePositions() {
        return tirePositions;
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



    @Override
    public void place(Vector2 position, float angle) {
        body.setTransform(position, angle);
        for(int i = 0; i < tirePositions.size(); i++){
            Tire t = tires.get(i);
            Vector2 pos = tirePositions.get(i);
            t.getBody().setTransform(body.getWorldPoint(pos), angle);
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        SpriteUtils.updateSprite(body, sprite, GameWorld.PIXELS_PER_METER);
        sprite.draw(batch);
        for(Tire t : tires){
            t.draw(batch);
        }
    }




    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getMaxTurnAngle() {
        return maxTurnAngle;
    }

    public void setMaxTurnAngle(float maxTurnAngle) {
        this.maxTurnAngle = maxTurnAngle;
    }


    public float getTurnDegreesPerSecond() {
        return turnDegreesPerSecond;
    }

    public void setTurnDegreesPerSecond(float turnDegreesPerSecond) {
        this.turnDegreesPerSecond = turnDegreesPerSecond;
    }




}
