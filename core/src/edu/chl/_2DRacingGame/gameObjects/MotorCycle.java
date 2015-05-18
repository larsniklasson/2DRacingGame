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
 * Created by Lars Niklasson on 2015-05-09.
 */
public class MotorCycle extends OurVehicle{



    private static final float TIRE_WIDTH = 4/GameWorld.PIXELS_PER_METER;
    private static final float TIRE_HEIGHT = 9/GameWorld.PIXELS_PER_METER;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 1.5f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.07f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.07f;


    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;



    public MotorCycle(World world) {
        super(world);


        Texture texture = Assets.mcBody;
        setSprite(new Sprite(texture));


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8f / GameWorld.PIXELS_PER_METER / 2, 24f / GameWorld.PIXELS_PER_METER / 2);
        createBody(shape, 0.1f);


        Texture tireTexture = Assets.mcTire;
        Tire frontTire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        frontTire.setSprite(new Sprite(tireTexture));

        frontTire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        attachTire(frontTire, new Vector2(0, 10 / GameWorld.PIXELS_PER_METER), true);


        Tire backTire = frontTire.cpy();
        backTire.setMaxLateralImpulse(MAX_LATERAL_IMPULSE_BACK);
        backTire.setDriveForce(DRIVE_FORCE_BACK_WHEELS);

        attachTire(backTire, new Vector2(0, -10 / GameWorld.PIXELS_PER_METER), false);

        setMaxTurnAngle(30f);

    }

    @Override
    public AISpeedHolder getEasySpeeds() {
        return AISpeedHolder.getStandardEasySpeed();  //TODO good values here
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
