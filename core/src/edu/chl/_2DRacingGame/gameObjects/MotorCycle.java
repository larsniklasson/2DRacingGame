package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.gameObjects.steering.AISpeedHolder;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Class for a motorcycle, a fast but weak vehicle.
 *@author Lars Niklasson
 */
public class MotorCycle extends WheeledVehicle {



    private static final float TIRE_WIDTH = 4/GameWorld.PIXELS_PER_METER;
    private static final float TIRE_HEIGHT = 9/GameWorld.PIXELS_PER_METER;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 1.5f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.07f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.07f;


    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;


    /**
     * Creates a motorcycle set in the specified Box2D-world.
     *
     * @param world the Box2D-world which the motorcycle will be created in.
     */
    public MotorCycle(World world) {
        super(world);


        Texture texture = Assets.mcBody;
        setSprite(new Sprite(texture));


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8f / GameWorld.PIXELS_PER_METER / 2, 24f / GameWorld.PIXELS_PER_METER / 2);
        createBody(shape, 0.1f);


        Texture wheelTexture = Assets.mcWheel;
        Wheel frontWheel = new Wheel(world, TIRE_WIDTH, TIRE_HEIGHT, 1);
        frontWheel.setSprite(new Sprite(wheelTexture));

        frontWheel.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        attachWheel(frontWheel, new Vector2(0, 10 / GameWorld.PIXELS_PER_METER), true);


        Wheel backWheel = frontWheel.cpy();
        backWheel.setMaxLateralImpulse(MAX_LATERAL_IMPULSE_BACK);
        backWheel.setDriveForce(DRIVE_FORCE_BACK_WHEELS);

        attachWheel(backWheel, new Vector2(0, -10 / GameWorld.PIXELS_PER_METER), false);

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
