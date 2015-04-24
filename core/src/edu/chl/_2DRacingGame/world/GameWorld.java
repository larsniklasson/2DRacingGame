package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Wall;

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

        float width = (Gdx.graphics.getWidth() - 20) / PIXELS_PER_METER;
        float height = (Gdx.graphics.getHeight() - 30) / PIXELS_PER_METER;


        new Wall(b2World, new Vector2(-width / 2, height / 2), new Vector2(width / 2, height / 2));
        new Wall(b2World, new Vector2(-width / 2, height / 2), new Vector2(-width / 2, -height / 2));
        new Wall(b2World, new Vector2(width / 2, height / 2), new Vector2(width / 2, -height / 2));
        new Wall(b2World, new Vector2(-width / 2, -height / 2), new Vector2(width / 2, -height / 2));

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
}
