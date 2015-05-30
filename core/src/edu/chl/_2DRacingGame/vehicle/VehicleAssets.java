package edu.chl._2DRacingGame.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * A class which handles the vehicles image assets
 * @author Anton Ingvarsson
 */
public class VehicleAssets {

    // Vehicle textures

    //car
    public static Texture carBody;
    public static Texture carWheel;

    //motorcycle

    public static Texture mcBody;
    public static Texture mcWheel;

    //monstertruck

    public static Texture monsterTruckBody;
    public static Texture monsterTruckWheel;

    //f1car

    public static Texture f1CarBody;
    public static Texture f1CarBackWheel;
    public static Texture f1CarFrontWheel;

    //Magic Carpet
    public static Texture magicCarpetBody;

    /**
     *
     * @param file the path of the file
     * @return  A texture of the file
     */
    private static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * Initialize the vehicles textures
     */
    public static void load() {

        //-----vehicles-------

        carBody = loadTexture("vehicles/car_body.png");
        carWheel = loadTexture("vehicles/car_wheel.png");

        mcBody = loadTexture("vehicles/mc_body.png");
        mcWheel = loadTexture("vehicles/mc_wheel.png");

        monsterTruckBody = loadTexture("vehicles/mt_body.png");
        monsterTruckWheel = loadTexture("vehicles/mt_wheel.png");

        f1CarBody = loadTexture("vehicles/f1_body.png");
        f1CarBackWheel = loadTexture("vehicles/f1_back_wheel.png");
        f1CarFrontWheel = loadTexture("vehicles/f1_front_wheel.png");

        magicCarpetBody = loadTexture("vehicles/magiccarpet2.png");

    }
}
