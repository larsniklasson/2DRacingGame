package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.Dirt;
import edu.chl._2DRacingGame.Ice;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.gameObjects.Wall;
import edu.chl._2DRacingGame.helperClasses.MyContactListener;

/**
 * Created by Lasse on 2015-04-21.
 */
public class GameWorld {

    private World b2World;
    public static float PIXELS_PER_METER = 20f;
    private Car car;

    public GameWorld(){

        b2World = new World(new Vector2(0, 0), true);

        car = new Car(b2World);


        //car.body.setTransform(0, 0, 0);



        float width = Gdx.graphics.getWidth()/PIXELS_PER_METER;
        float height = Gdx.graphics.getHeight()/PIXELS_PER_METER;


        car.body.setTransform(width / 2, height / 2, 0);
        for(Tire t : car.getTires()){
            t.getBody().setTransform(width/2, height/2, 0);
        }

        new Wall(b2World, new Vector2(0,0), new Vector2(0,height));
        new Wall(b2World, new Vector2(0,0), new Vector2(width,0));
        new Wall(b2World, new Vector2(0,height), new Vector2(width,height));
        new Wall(b2World, new Vector2(width,0), new Vector2(width,height));

        PolygonShape dirtShape = new PolygonShape();
        dirtShape.setAsBox(100/PIXELS_PER_METER, 200/PIXELS_PER_METER, new Vector2(width/2,height/2),0);
        new TrackSection(b2World, dirtShape, new Ice());

        b2World.setContactListener(new MyContactListener());

    }

    public World getb2World(){
        return b2World;
    }

    public void update(float delta) {


        pollForInput();
        car.update();

    }


    //this should be in another class
    private void pollForInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            car.key = Input.Keys.UP;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            car.key = Input.Keys.DOWN;
        } else {
            car.key = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            car.turn = Input.Keys.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            car.turn = Input.Keys.RIGHT;
        } else {
            car.turn = 0;
        }



    }

    public Car getCar(){
        return car;
    }
}
