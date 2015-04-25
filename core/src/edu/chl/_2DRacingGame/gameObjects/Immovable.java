package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Lasse on 2015-04-21.
 */
public class Immovable {

    Body body;

    public Immovable(World world, Vector2 start, Vector2 end){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        body = world.createBody(bodyDef);

        EdgeShape shape = new EdgeShape();
        shape.set(start.x, start.y, end.x, end.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;


        fixtureDef.isSensor = false;

        body.createFixture(fixtureDef);





    }
}
