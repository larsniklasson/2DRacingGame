package edu.chl._2DRacingGame;


import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Lars Niklasson on 2015-04-24.
 */
public class TrackSection {


    private GroundMaterial groundMaterial;

    public TrackSection(World world, Shape shape, GroundMaterial groundMaterial){
        this.groundMaterial = groundMaterial;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);
        body.setUserData(this);




        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;



        body.createFixture(fixtureDef);
    }

    public GroundMaterial getGroundMaterial(){
        return groundMaterial;
    }

}
