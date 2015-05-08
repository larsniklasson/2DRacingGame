package edu.chl._2DRacingGame.gameObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
 * Created by Lars Niklasson on 2015-05-07.
 */
public class MagicCarpet implements Vehicle{

    private static final float WIDTH = 20f/GameWorld.PIXELS_PER_METER;
    private static final float HEIGHT = 30f/GameWorld.PIXELS_PER_METER;

    private Body body;
    private Tire tire;
    private Sprite sprite;


    public MagicCarpet(World world) {



        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH/2, HEIGHT/2);

        body.createFixture(shape, 0.001f);


        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        tire = new FlyingTire(world, WIDTH, HEIGHT, 0.5f);
        tire.setCharacteristics(10f, 0.2f, 15f, -5f, -0.2f);



        jointDef.bodyB = tire.getBody();

        jointDef.localAnchorA.set(new Vector2(0, 0));

        world.createJoint(jointDef);





        Texture texture = new Texture(Gdx.files.internal("magiccarpet.png"));
        sprite = new Sprite(texture);




    }




    public void update(Set<InputManager.PressedKey> keys){
        tire.update(keys);

        float torque = 0;
        if(keys.contains(InputManager.PressedKey.Left)){
            torque = 3;
        }

        if (keys.contains(InputManager.PressedKey.Right)){
            torque = -3;
        }

        tire.getBody().applyAngularImpulse(0.1f * tire.getBody().getInertia() * -tire.getBody().getAngularVelocity(), true);
        tire.getBody().applyTorque(torque, true);

        updateSprite();

    }

    private void updateSprite() {
        sprite.setPosition((tire.getBody().getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (tire.getBody().getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(tire.getBody().getAngle()));
    }

    @Override
    public List<Sprite> getSprites() {
        List<Sprite> list = new ArrayList<>();
        list.add(sprite);
        return list;
    }

    @Override
    public void place(Vector2 position, float angle) {
        body.setTransform(position, angle);
        tire.getBody().setTransform(position, angle);
        updateSprite();

    }

    @Override
    public Body getBody() {
        return body;
    }


}
