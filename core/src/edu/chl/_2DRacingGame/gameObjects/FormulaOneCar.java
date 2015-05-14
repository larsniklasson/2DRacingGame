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
public class FormulaOneCar extends Vehicle{

    //comment out debugrenderer for full viewing pleasure

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

    public FormulaOneCar(World world) {
        super(world);

        Texture texture = new Texture(Gdx.files.internal("f1_body.png"));
        setSprite(new Sprite(texture));

        setMaxAngle(MAX_ANGLE);

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

        Texture frontTireTexture = new Texture(Gdx.files.internal("f1_front_tire.png"));
        Texture backTireTexture = new Texture(Gdx.files.internal("f1_back_tire.png"));


        Tire frontTire = new Tire(world,TIRE_WIDTH_FRONT, TIRE_HEIGHT_FRONT, 1f);
        frontTire.setCharacteristics(DRIVE_FORCE_FRONT_WHEELS, MAX_LATERAL_IMPULSE_FRONT, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        frontTire.setSprite(new Sprite(frontTireTexture));
        attachTire(frontTire, new Vector2(-7 / SCALE, 32 / SCALE), true);


        attachTire(frontTire.cpy(), new Vector2(7 / SCALE, 32 / SCALE), true);

        Tire backTire = new Tire(world,TIRE_WIDTH_BACK, TIRE_HEIGHT_BACK, 1f);
        backTire.setCharacteristics(DRIVE_FORCE_BACK_WHEELS, MAX_LATERAL_IMPULSE_BACK, MAX_FORWARD_SPEED, MAX_BACKWARD_SPEED, BACKWARDS_FRICTION);
        backTire.setSprite(new Sprite(backTireTexture));
        attachTire(backTire, new Vector2(-7 / SCALE, 8 / SCALE), false);


        attachTire(backTire.cpy(),new Vector2(7/SCALE, 8/SCALE),false);




    }
}
