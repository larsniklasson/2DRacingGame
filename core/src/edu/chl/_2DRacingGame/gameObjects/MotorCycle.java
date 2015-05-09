package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lars Niklasson on 2015-05-09.
 */
public class MotorCycle extends Vehicle{


    private static final float SCALE = 0.4f * GameWorld.PIXELS_PER_METER;
    private static final float TIRE_WIDTH = 0.75f * 2 / SCALE;
    private static final float TIRE_HEIGHT = 1.75f * 2 / SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 1.5f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.07f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.07f;


    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;



    public MotorCycle(World world) {
        super(world);

        Texture texture = new Texture(Gdx.files.internal("mc_body.png"));
        setSprite(new Sprite(texture));


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8f / GameWorld.PIXELS_PER_METER / 2, 24f / GameWorld.PIXELS_PER_METER / 2);
        createBody(shape, 0.1f);


        Texture tireTexture = new Texture(Gdx.files.internal("mc_tire.png"));
        Tire tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        tire.setSprite(new Sprite(tireTexture));

        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        attachTire(tire, new Vector2(0, 10 / GameWorld.PIXELS_PER_METER), true);


        tire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        tire.setSprite(new Sprite(tireTexture));

        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        attachTire(tire, new Vector2(0, -10 / GameWorld.PIXELS_PER_METER), false);

        setMaxAngle(30f);

    }
}
