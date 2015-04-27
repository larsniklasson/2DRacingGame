package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.Dirt;
import edu.chl._2DRacingGame.Ice;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.controllers.CheckpointController;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.gameObjects.Immovable;
import edu.chl._2DRacingGame.helperClasses.MathHelper;
import edu.chl._2DRacingGame.controllers.ContactController;
import edu.chl._2DRacingGame.models.Checkpoint;
import edu.chl._2DRacingGame.models.CheckpointDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameWorld {

    private World b2World;


    public static float PIXELS_PER_METER = 20f;  //box2d scale factor.
    private Car car;
    private TiledMap tiledMap;

    private final CheckpointController checkpointController;

    public GameWorld(){

        tiledMap = new TmxMapLoader().load("map2.tmx");

        b2World = new World(new Vector2(0, 0), true);

        car = new Car(b2World);





        car.body.setTransform(20 / PIXELS_PER_METER, 20 / PIXELS_PER_METER, 0);
        for(Tire t : car.getTires()){
            t.getBody().setTransform(20/PIXELS_PER_METER, 20/PIXELS_PER_METER, 0);
        }

        List<Checkpoint> checkpoints = new ArrayList<>();

        Checkpoint cp1 = new Checkpoint(new Vector2(5, 5), new Vector2(10, 5), b2World);
        cp1.addAllowedPassingDirection(CheckpointDirection.SOUTH);
        checkpoints.add(cp1);

        Checkpoint cp2 = new Checkpoint(new Vector2(5, 20), new Vector2(20, 20), b2World);
        cp2.addAllowedPassingDirection(CheckpointDirection.SOUTH);
        checkpoints.add(cp2);

        checkpointController = new CheckpointController(checkpoints);

        b2World.setContactListener(new ContactController((car, checkpoint, validEntry) -> {
            if (validEntry) {
                checkpointController.validPassing(car, checkpoint);
            } else {
                checkpointController.invalidPassing(car, checkpoint);
            }
        }));

        createShapesFromMap();
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

                if(object.getName().equals("dirt")){


                    new TrackSection(b2World, objectToShape(object), new Dirt());
                    //createTrackSection(object, new Dirt());
                } else if(object.getName().equals("ice")){
                    new TrackSection(b2World, objectToShape(object), new Ice());
                    //createTrackSection(object, new Ice());
                } else if (object.getName().equals("solid")){
                    new Immovable(b2World, objectToShape(object));

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


    //should probably be in another class
    public Shape objectToShape(MapObject object){


        if(object instanceof RectangleMapObject){

            Rectangle r = ((RectangleMapObject)object).getRectangle();
            MathHelper.scaleRect(r, 1/PIXELS_PER_METER);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(r.getWidth()/2, r.getHeight()/2, r.getCenter(new Vector2()), 0);
            return shape;


        }



        if(object instanceof PolygonMapObject){
            Polygon p = ((PolygonMapObject)object).getPolygon();
            float x = p.getX()/PIXELS_PER_METER;
            float y = p.getY()/PIXELS_PER_METER;

            float[] vertices = p.getVertices();
            for(int i = 0; i < vertices.length; i ++){
                vertices[i] = vertices[i] / PIXELS_PER_METER;
                if(i % 2 == 0){
                    vertices[i] += x;
                } else {
                    vertices[i] += y;
                }
            }

            PolygonShape shape = new PolygonShape();
            shape.set(vertices);
            return shape;




        }

        //only works for circles

        if(object instanceof EllipseMapObject){
            Ellipse e = ((EllipseMapObject)object).getEllipse();

            CircleShape shape = new CircleShape();

            shape.setRadius(e.width / 2 / PIXELS_PER_METER);
            shape.setPosition(new Vector2(e.x / PIXELS_PER_METER + shape.getRadius(), e.y / PIXELS_PER_METER + shape.getRadius()));
            return shape;



        }

        //only works for a straight line with only 2 vertices

        if(object instanceof PolylineMapObject){
            Polyline pl = ((PolylineMapObject) object).getPolyline();

            float[] vertices = pl.getVertices();

            for(int i = 0; i < vertices.length; i ++){
                vertices[i] = vertices[i]/PIXELS_PER_METER;
            }

            float x = pl.getX()/PIXELS_PER_METER;
            float y = pl.getY()/PIXELS_PER_METER;

            for(int i = 0; i < vertices.length; i++){
                if(i%2 == 0){
                    vertices[i] += x;
                } else {
                    vertices[i] += y;
                }
            }
            EdgeShape shape = new EdgeShape();
            shape.set(vertices[0], vertices[1], vertices[2], vertices[3]);

            return shape;

        }

        return null;


    }
}
