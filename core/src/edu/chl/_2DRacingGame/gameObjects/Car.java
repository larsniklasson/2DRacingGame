package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lars Niklasson on 2015-05-08.
 */
public class Car extends Vehicle {

    private static final float SCALE = 0.4f * GameWorld.PIXELS_PER_METER;
    private static final float TIRE_WIDTH = 0.5f * 2 / SCALE;
    private static final float TIRE_HEIGHT = 1.25f * 2 / SCALE;


    private static final float DRIVE_FORCE_FRONT_WHEELS = 2f;
    private static final float DRIVE_FORCE_BACK_WHEELS = 1f;

    private static final float MAX_LATERAL_IMPULSE_FRONT = 0.05f;
    private static final float MAX_LATERAL_IMPULSE_BACK = 0.05f;



    private static final float MAX_FORWARD_SPEED = 17;
    private static final float MAX_BACKWARD_SPEED = -7;

    private static final float BACKWARDS_FRICTION = -0.02f;




    public Car(World world) {
        super(world);

        Vector2[] vertices = new Vector2[8];

        vertices[0] = new Vector2(1.5f / SCALE, 0);
        vertices[1] = new Vector2(3 / SCALE, 2.5f / SCALE);
        vertices[2] = new Vector2(2.8f / SCALE, 5.5f / SCALE);
        vertices[3] = new Vector2(1 / SCALE, 10 / SCALE);
        vertices[4] = new Vector2(-1 / SCALE, 10 / SCALE);
        vertices[5] = new Vector2(-2.8f / SCALE, 5.5f / SCALE);
        vertices[6] = new Vector2(-3 / SCALE, 2.5f / SCALE);
        vertices[7] = new Vector2(-1.5f / SCALE, 0 / SCALE);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        createBody(shape, 0.1f);

        Texture texture = new Texture(Gdx.files.internal("carbody.png"));
        setSprite(new Sprite(texture));


        Tire frontTire = new Tire(world, TIRE_WIDTH, TIRE_HEIGHT, 1.0f);
        Texture tireTexture = new Texture(Gdx.files.internal("tire.png"));
        frontTire.setSprite(new Sprite(tireTexture));
        frontTire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);

        Tire backTire = frontTire.cpy();
        backTire.setDriveForce(DRIVE_FORCE_BACK_WHEELS);
        backTire.setMaxLateralImpulse(MAX_LATERAL_IMPULSE_BACK);


        attachTire(frontTire, new Vector2(-3f / SCALE, 8.5f / SCALE), true);

        attachTire(frontTire.cpy(), new Vector2(3f / SCALE, 8.5f / SCALE), true);

        attachTire(backTire, new Vector2(3f / SCALE, 0), false);

        attachTire(backTire.cpy(), new Vector2(-3f / SCALE, 0), false);




    }
}
