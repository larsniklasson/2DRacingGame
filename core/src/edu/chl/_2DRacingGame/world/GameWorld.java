package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
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
import edu.chl._2DRacingGame.helperClasses.MathHelper;
import edu.chl._2DRacingGame.helperClasses.MyContactListener;

/**
 * Created by Lasse on 2015-04-21.
 */
public class GameWorld {

    private World b2World;
    public static float PIXELS_PER_METER = 20f;
    private Car car;

    private TiledMap tiledMap;

    public GameWorld(){

        tiledMap = new TmxMapLoader().load("map.tmx");


        b2World = new World(new Vector2(0, 0), true);

        car = new Car(b2World);


        //car.body.setTransform(0, 0, 0);



        float width = Gdx.graphics.getWidth()/PIXELS_PER_METER;
        float height = Gdx.graphics.getHeight()/PIXELS_PER_METER;


        car.body.setTransform(width / 2, height / 2, 0);
        for(Tire t : car.getTires()){
            t.getBody().setTransform(width / 2, height / 2, 0);
        }

        new Wall(b2World, new Vector2(0,0), new Vector2(0,height));
        new Wall(b2World, new Vector2(0,0), new Vector2(width,0));
        new Wall(b2World, new Vector2(0,height), new Vector2(width,height));
        new Wall(b2World, new Vector2(width,0), new Vector2(width,height));

        //adding studd from tilemap. hardcoding
        MapObjects mo = tiledMap.getLayers().get("objects").getObjects();

        //dirt rectangle

        Rectangle dirtRect = ((RectangleMapObject)mo.get("dirt")).getRectangle();
        MathHelper.scaleRect(dirtRect, 1 / PIXELS_PER_METER);

        PolygonShape dirtShape = new PolygonShape();
        dirtShape.setAsBox(dirtRect.getWidth()/2, dirtRect.getHeight()/2, dirtRect.getCenter(new Vector2()), 0);

        new TrackSection(b2World, dirtShape, new Dirt());

        //ice rectangle
        Rectangle iceRect = ((RectangleMapObject)mo.get("ice")).getRectangle();
        MathHelper.scaleRect(iceRect, 1 / PIXELS_PER_METER);

        PolygonShape iceShape = new PolygonShape();
        iceShape.setAsBox(iceRect.getWidth()/2, iceRect.getHeight()/2, iceRect.getCenter(new Vector2()), 0);

        new TrackSection(b2World, iceShape, new Ice());

        //ice triangle 1

        Polygon icePoly = ((PolygonMapObject)mo.get("icet1")).getPolygon();



        float[] vertices = icePoly.getVertices();
        float x = icePoly.getX()/PIXELS_PER_METER;
        float y = icePoly.getY()/PIXELS_PER_METER;

        for (int i = 0; i < vertices.length; i++) {

            vertices[i] = vertices[i] /PIXELS_PER_METER;
            System.out.println(vertices[i]);
        }

        Vector2[] v2verts = new Vector2[3];
        v2verts[0] = new Vector2(x +vertices[0], y + vertices[1]);
        v2verts[1] = new Vector2(x + vertices[2], y + vertices[3]);
        v2verts[2] = new Vector2(x + vertices[4], y + vertices[5]);

        for(Vector2 v: v2verts){
            System.out.println("x = " + v.x);
            System.out.println("y = " + v.y);
        }

        PolygonShape ps = new PolygonShape();
        ps.set(v2verts);
        new TrackSection(b2World, ps, new Ice());


        //ice triangle 2

        Polygon icePoly2 = ((PolygonMapObject)mo.get("icet2")).getPolygon();



        vertices = icePoly2.getVertices();
        x = icePoly2.getX()/PIXELS_PER_METER;
        y = icePoly2.getY()/PIXELS_PER_METER;

        for (int i = 0; i < vertices.length; i++) {

            vertices[i] = vertices[i] /PIXELS_PER_METER;
            System.out.println(vertices[i]);
        }

        v2verts = new Vector2[3];
        v2verts[0] = new Vector2(x +vertices[0], y + vertices[1]);
        v2verts[1] = new Vector2(x + vertices[2], y + vertices[3]);
        v2verts[2] = new Vector2(x + vertices[4], y + vertices[5]);

        for(Vector2 v: v2verts){
            System.out.println("x = " + v.x);
            System.out.println("y = " + v.y);
        }

        ps = new PolygonShape();
        ps.set(v2verts);
        new TrackSection(b2World, ps, new Ice());


        //ice rekt 2

        iceRect = ((RectangleMapObject)mo.get("icerekt")).getRectangle();
        MathHelper.scaleRect(iceRect, 1 / PIXELS_PER_METER);

        iceShape = new PolygonShape();
        iceShape.setAsBox(iceRect.getWidth() / 2, iceRect.getHeight() / 2, iceRect.getCenter(new Vector2()), 0);

        new TrackSection(b2World, iceShape, new Ice());



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

    public TiledMap getTiledMap(){
        return tiledMap;
    }
}
