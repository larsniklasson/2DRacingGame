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
public class MonsterTruck extends Vehicle {

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


    public MonsterTruck(World world) {
        super(world);

        Texture bodyTexture = new Texture(Gdx.files.internal("mt_body.png"));
        setSprite(new Sprite(bodyTexture));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);

        createBody(shape, 0.1f);


        Texture tireTexture = new Texture(Gdx.files.internal("mt_tire.png"));

        Tire tire = new Tire(world,TIRE_WIDTH, TIRE_HEIGHT,1f);
        tire.setSprite(new Sprite(tireTexture));
        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        attachTire(tire, new Vector2(-16 / SCALE, 16 / SCALE), true);

        tire = new Tire(world,TIRE_WIDTH, TIRE_HEIGHT,0.5f);
        tire.setSprite(new Sprite(tireTexture));
        tire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        attachTire(tire, new Vector2(16 / SCALE, 16 / SCALE), true);

        tire = new Tire(world,TIRE_WIDTH, TIRE_HEIGHT,0.5f);
        tire.setSprite(new Sprite(tireTexture));
        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        attachTire(tire, new Vector2(-16 / SCALE, -16 / SCALE), false);

        tire = new Tire(world,TIRE_WIDTH, TIRE_HEIGHT,0.5f);
        tire.setSprite(new Sprite(tireTexture));
        tire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS,MAX_LATERAL_IMPULSE_BACK,MAX_FORWARD_SPEED,MAX_BACKWARD_SPEED,BACKWARDS_FRICTION);
        attachTire(tire,new Vector2(16/SCALE, -16/SCALE),false);

    }
}
