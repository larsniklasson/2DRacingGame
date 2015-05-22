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
 * Class for a magic carpet-vehicle, which is a flying vehicle.
 * Is designed using one big Wheel used to control the vehicle.
 *
 * NOTE: Should not use the WheelSteeringSystem directly but rather the FlyingSteeringSystem, which is designed for
 * flying vehicles such as this one
 *
 * Uses FlyingWheels to "fly" over ground-materials.
 *
 * @author Lars Niklasson
 */
public class MagicCarpet extends WheeledVehicle {

    private static final float WIDTH = 20f/ GameWorld.PIXELS_PER_METER;
    private static final float HEIGHT = 30f/GameWorld.PIXELS_PER_METER;


    /**
     * Creates a magic carpet set in the specified Box2D-world.
     *
     * @param world the Box2D-world which the magic carpet will be created in.
     */
    public MagicCarpet(World world) {
        super(world);


        Texture texture = Assets.magicCarpetBody;
        setSprite(new Sprite(texture));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);

        createBody(shape, 0.0001f);

        Wheel wheel = new FlyingWheel(world, WIDTH, HEIGHT, 0.5f);
        wheel.setCharacteristics(10f, 0.2f, 15f, -5f, -0.2f);

        attachWheel(wheel, new Vector2(0, 0), false);
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
