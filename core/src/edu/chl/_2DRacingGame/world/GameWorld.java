package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.Dirt;
import edu.chl._2DRacingGame.Ice;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.controllers.CheckpointController;
import edu.chl._2DRacingGame.controllers.ContactController;
import edu.chl._2DRacingGame.gameModes.GameListener;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameModes.TimeTrial;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Immovable;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.helperClasses.CheckpointFactory;
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.models.Checkpoint;
import edu.chl._2DRacingGame.models.CheckpointDirection;
import edu.chl._2DRacingGame.models.CheckpointType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameWorld implements GameListener {

    public static float PIXELS_PER_METER = 20f;  //box2d scale factor.

    private Car car;
    private List<Checkpoint> checkpoints = new ArrayList<>();

    private World b2World;
    private TiledMap tiledMap;

    private final CheckpointController checkpointController;
    private final GameMode gameMode = new TimeTrial(this);

    public GameWorld(){

        tiledMap = new TmxMapLoader().load("map2.tmx");

        b2World = new World(new Vector2(0, 0), true);

        car = new Car(b2World);

        car.body.setTransform(20 / PIXELS_PER_METER, 20 / PIXELS_PER_METER, 0);
        for(Tire t : car.getTires()){
            t.getBody().setTransform(20/PIXELS_PER_METER, 20/PIXELS_PER_METER, 0);
        }

        createShapesFromMap();

        checkpointController = new CheckpointController(gameMode,  checkpoints);

        b2World.setContactListener(new ContactController((car, checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(car, checkpoint);
            } else {
                checkpointController.invalidPassing(car, checkpoint);
            }
        }));
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

    private void createShapesFromMap(){

        MapLayers ml = tiledMap.getLayers();
        Iterator<MapLayer> it = ml.iterator();

        while(it.hasNext()){
            MapLayer layer = it.next();
            MapObjects mo = layer.getObjects();
            Iterator<MapObject> it2 = mo.iterator();

            while(it2.hasNext()){
                MapObject object = it2.next();
                Shape shape = ShapeFactory.createShape(object, PIXELS_PER_METER);

                if(object.getName().equals("dirt")){
                    new TrackSection(b2World, shape, new Dirt());
                } else if(object.getName().equals("ice")){
                    new TrackSection(b2World, shape, new Ice());
                } else if (object.getName().equals("solid")){
                    new Immovable(b2World, shape);
                } else if (object.getProperties().get("type").equals("checkpoint")) {

                    CheckpointType type = CheckpointType.getTypeFromName(
                        (String) object.getProperties().get("checkpointType")
                    );

                    CheckpointDirection direction = CheckpointDirection.getDirectionFromName(
                            (String) object.getProperties().get("checkpointDirection")
                    );
                    Checkpoint cp = CheckpointFactory.createCheckpoint(b2World, shape, type);
                    cp.addAllowedPassingDirection(direction);
                    checkpoints.add(cp);

                }

            }
        }

    }

    public Car getCar(){
        return car;
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void gameFinished(String message) {
        System.out.println("GAME DONE: " + message); // TODO display restart screen such
    }
}
