package edu.chl._2DRacingGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Lasse on 2015-04-24.
 */
public class TrackSection {

    private Body body;
    private GroundMaterial groundMaterial;

    public TrackSection(World world, Shape shape, GroundMaterial groundMaterial){
        this.groundMaterial = groundMaterial;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bodyDef);
        body.setUserData(this);


        /*
        float x = r.getX();
        float y = r.getY();
        float h = r.getHeight();
        float w = r.getWidth();

        Vector2 a = new Vector2(x,y);
        Vector2 b = new Vector2(x + w, y);
        Vector2 c = new Vector2(x + w, y + h);
        Vector2 d = new Vector2(x, y + h);


        Vector2[] vertices = {a,b,c,d};

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);*/




        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;



        body.createFixture(fixtureDef);
    }

}
