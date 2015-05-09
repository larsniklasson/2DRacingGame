package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import edu.chl._2DRacingGame.Dirt;
import edu.chl._2DRacingGame.Ice;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.controllers.CheckpointController;
import edu.chl._2DRacingGame.controllers.ContactController;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.helperClasses.CheckpointFactory;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.helperClasses.MathHelper;
import edu.chl._2DRacingGame.helperClasses.ShapeFactory;
import edu.chl._2DRacingGame.models.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameWorld implements Disposable {

    private final Player player;

    public static float PIXELS_PER_METER = 20f;  //box2d scale factor.




    private final GameMode gameMode;
    private final List<Checkpoint> checkpoints = new ArrayList<>();

    private World b2World;
    private TiledMap tiledMap;

    private final CheckpointController checkpointController;

    public GameWorld(Player player, GameMap gameMap, GameMode gameMode) {
        this.gameMode = gameMode;
        tiledMap = gameMap.load();
        b2World = new World(new Vector2(0, 0), true);

        // TODO insane. Vehicle should probably be injected directly when
        // the car class is ready for it.
        this.player = player;


        //player.setVehicle(new MotorCycle(b2World));
        //player.setVehicle(new Car(b2World));
        //player.setVehicle(new MagicCarpet(b2World));
        //player.setVehicle(new Car2(b2World));
        //player.setVehicle(new MotorCycle2(b2World));
        player.setVehicle(new MagicCarpet2(b2World));

        checkpointController = new CheckpointController(this.gameMode, checkpoints);

        createShapesFromMap();

        b2World.setContactListener(new ContactController((vehicle, checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(vehicle, checkpoint);
            } else {
                checkpointController.invalidPassing(vehicle, checkpoint);
            }
        }));
    }

    public World getb2World(){
        return b2World;
    }

    public void update(float delta) {
        b2World.step(delta, 3, 3);
        Set<InputManager.PressedKey> keys = InputManager.pollForInput();
        player.getVehicle().update(keys);
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

                } else if(object.getName().equals("start")){

                    Rectangle r = ((RectangleMapObject) object).getRectangle();


                    String s = (String) object.getProperties().get("type");

                    float angle;
                    switch(s){
                        case "NORTH":
                            angle = 0;
                            break;
                        case "SOUTH":
                            angle = (float) Math.PI;
                            break;
                        case "WEST":
                            angle = (float) (Math.PI/2);
                            break;
                        case "EAST":

                            angle = (float) (3 * Math.PI/2);
                            break;
                        default:
                            angle = 0;
                            break;
                    }
                    player.getVehicle().place(r.getCenter(new Vector2()), angle);



                }

            }
        }

    }

    public Car getCar(){
        //return car;
        return null;
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void dispose() {
        b2World.dispose();
        tiledMap.dispose();
    }
}
