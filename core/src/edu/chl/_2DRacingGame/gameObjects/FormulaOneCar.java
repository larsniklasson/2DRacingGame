package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.steering.AISpeedHolder;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Class for a Formula one car, fast but bad in tight spaces.
 *
 * @author Lars Niklasson
 */
public class FormulaOneCar extends OurVehicle{


    private static final float SCALE = GameWorld.PIXELS_PER_METER;

    private static final float TIRE_WIDTH_FRONT = 4/SCALE;
    private static final float TIRE_HEIGHT_FRONT = 6/SCALE;


    private static final float TIRE_WIDTH_BACK = 5/SCALE;
    private static final float TIRE_HEIGHT_BACK = 6/SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 2f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 2f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.08f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.08f;



    private static final float MAX_FORWARD_SPEED = 20;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;

    private static final float MAX_ANGLE = 25f;

    /**
     * {@inheritDoc}
     */
    public FormulaOneCar(World world) {
        super(world);

        Texture texture = Assets.f1CarBody;
        setSprite(new Sprite(texture));



        setMaxTurnAngle(MAX_ANGLE);


        Vector2[] vertices = new Vector2[6];

        vertices[0] = new Vector2(4 / SCALE, 0);
        vertices[1] = new Vector2(8 / SCALE, 6 / SCALE);
        vertices[2] = new Vector2(10 / SCALE, 42 / SCALE);
        vertices[3] = new Vector2(-10 / SCALE, 42 / SCALE);
        vertices[4] = new Vector2(-8 / SCALE, 6 / SCALE);
        vertices[5] = new Vector2(-4 / SCALE, 0 / SCALE);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        createBody(shape, 0.1f);

        Texture frontWheelTexture = Assets.f1CarFrontWheel;
        Texture backWheelTexture = Assets.f1CarBackWheel;


        Wheel frontWheel = new Wheel(world,TIRE_WIDTH_FRONT, TIRE_HEIGHT_FRONT, 1f);
        frontWheel.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        frontWheel.setSprite(new Sprite(frontWheelTexture));
        attachWheel(frontWheel, new Vector2(-7 / SCALE, 32 / SCALE), true);


        attachWheel(frontWheel.cpy(), new Vector2(7 / SCALE, 32 / SCALE), true);

        Wheel backWheel = new Wheel(world,TIRE_WIDTH_BACK, TIRE_HEIGHT_BACK, 1f);
        backWheel.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        backWheel.setSprite(new Sprite(backWheelTexture));
        attachWheel(backWheel, new Vector2(-7 / SCALE, 8 / SCALE), false);


        attachWheel(backWheel.cpy(),new Vector2(7/SCALE, 8/SCALE),false);




    }

    @Override
    public AISpeedHolder getEasySpeeds() {
        return AISpeedHolder.getStandardEasySpeed(); //TODO good values here, placeholder values for now
    }

    @Override
    public AISpeedHolder getMediumSpeeds() {
        return AISpeedHolder.getStandardMediumSpeed();
    }

    @Override
    public AISpeedHolder getHardSpeeds() {
        return AISpeedHolder.getStandardHardSpeed();
    }
}
