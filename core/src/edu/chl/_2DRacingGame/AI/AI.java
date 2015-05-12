package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.world.GameWorld;


/**
 * Created by Kethupflaskaa on 2015-05-08.
 */
public class AI {

    private World world;
    private Sprite sprite;
    private Texture AITexture;
    private Body body;
    FollowPath<Vector2, LinePath.LinePathParam> followPath;
    AICar aiCar;

    public static float PIXELS_PER_METER = 20f;

    LinePath<Vector2> linePath;


    private static final float SCALE = 0.4f * GameWorld.PIXELS_PER_METER;

    public AI(World world){

        this.world = world;

        AITexture = new Texture(Gdx.files.internal("carbody.png"));

        sprite = new Sprite(AITexture);

        createBody();

        linePath = new LinePath<Vector2>(createPath(), false);
        followPath = new FollowPath<Vector2, LinePath.LinePathParam>(aiCar,linePath,10);

        aiCar.setSteeringBehavior(followPath);

        aiCar = new AICar();

    }

    private void updateSprite(){
        sprite.setPosition((body.getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (body.getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

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

        body.createFixture(shape, 0.1f);
    }

    public void moveTo(Vector2 position, float angle) {
        body.setTransform(position, angle);
    }

    public void update(){
        updateSprite();
    }

    private Array<Vector2> createPath(){
        Array<Vector2> wayPoints = new Array<>(10);

        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));
        wayPoints.add(new Vector2(100f / PIXELS_PER_METER, 50f / PIXELS_PER_METER));

        return wayPoints;
    }
}
