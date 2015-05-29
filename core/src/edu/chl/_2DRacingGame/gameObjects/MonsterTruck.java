package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.gameObjects.steering.AISpeedHolder;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Class for a Monster Truck, a slow but powerful vehicle.
 *@author Lars Niklasson
 */
public class MonsterTruck extends WheeledVehicle {

    private static final float SCALE = GameWorld.PIXELS_PER_METER;
    private static final float TIRE_WIDTH = 8 / SCALE;
    private static final float TIRE_HEIGHT = 14 / SCALE;

    private static final float WIDTH = 20f/SCALE;
    private static final float HEIGHT = 45f/SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 6f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 5f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.2f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.2f;



    private static final float MAX_FORWARD_SPEED = 12;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.03f;

    /**
     * Creates a monster truck set in the specified Box2D-world.
     *
     * @param world the Box2D-world which the monster truck will be created in.
     */
    public MonsterTruck(World world) {
        super(world);

        setTurnDegreesPerSecond(300);


        Texture bodyTexture = VehicleAssets.monsterTruckBody;
        setSprite(new Sprite(bodyTexture));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);

        createBody(shape, 0.1f);


        Texture wheelTexture = VehicleAssets.monsterTruckWheel;

        Wheel frontWheel = new Wheel(world,TIRE_WIDTH, TIRE_HEIGHT,1f);
        frontWheel.setSprite(new Sprite(wheelTexture));
        frontWheel.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        Wheel backWheel = frontWheel.cpy();
        backWheel.setMaxLateralImpulse(MAX_LATERAL_IMPULSE_BACK);
        backWheel.setDriveForce(DRIVE_FORCE_BACK_WHEELS);

        attachWheel(frontWheel, new Vector2(-16 / SCALE, 16 / SCALE), true);

        attachWheel(frontWheel.cpy(), new Vector2(16 / SCALE, 16 / SCALE), true);

        attachWheel(backWheel, new Vector2(-16 / SCALE, -16 / SCALE), false);

        attachWheel(backWheel.cpy(), new Vector2(16 / SCALE, -16 / SCALE), false);

    }

    @Override
    public AISpeedHolder getEasySpeeds() {
        return AISpeedHolder.getStandardEasySpeed();
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
