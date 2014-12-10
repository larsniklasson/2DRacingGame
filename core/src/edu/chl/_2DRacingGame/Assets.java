package edu.chl._2DRacingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;


/**
 * Created by Anton on 2015-04-27.
 */
public class Assets {

    // Vehicle textures

    //car
    public static Texture carBody;
    public static Texture carTire;

    //motorcycle

    public static Texture mcBody;
    public static Texture mcTire;

    //monstertruck

    public static Texture monsterTruckBody;
    public static Texture monsterTruckTire;

    //f1car

    public static Texture f1CarBody;
    public static Texture f1CarBackTire;
    public static Texture f1CarFrontTire;

    //magic carpet

    public static Texture magicCarpetBody;



    public static Texture mainMenuBackground;
    public static Texture pauseMenu;
    public static Sound carHorn;
    public static Texture ice;
    public static Texture dirt;
    public static Texture ful;
    public static Texture magicCarpet;
    public static Texture motorCycle;
    public static Texture monsterTruck;
    public static Texture formula1;
    public static Texture car;
    public static BitmapFont arial40;
    public static Image [] mapArray;
    public static Image [] vehicleArray;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load(){

        //-----vehicles-------

        carBody = loadTexture("vehicles/car_body.png");
        carTire = loadTexture("vehicles/car_tire.png");

        mcBody = loadTexture("vehicles/mc_body.png");
        mcTire = loadTexture("vehicles/mc_tire.png");

        monsterTruckBody = loadTexture("vehicles/mt_body.png");
        monsterTruckTire = loadTexture("vehicles/mt_tire.png");

        f1CarBody = loadTexture("vehicles/f1_body.png");
        f1CarBackTire = loadTexture("vehicles/f1_back_tire.png");
        f1CarFrontTire = loadTexture("vehicles/f1_front_tire.png");

        magicCarpetBody = loadTexture("vehicles/magiccarpet2.png");

        //-------------------


        //--- stuff that will probably be deleted
        mainMenuBackground = loadTexture("mainMenuBackground.jpg");
        pauseMenu = loadTexture("pauseMenu.png");
        carHorn = Gdx.audio.newSound(Gdx.files.internal("carHorn.wav"));
        ice = loadTexture("ice.png");
        dirt = loadTexture("dirt.png");
        ful = loadTexture("ful.png");


        //--------Images for Vehicle Selection
        magicCarpet = loadTexture("magicCarpet.jpg");
        motorCycle = loadTexture("Motorcycle.jpg");
        car = loadTexture("car.png");
        monsterTruck = loadTexture("monsterTruck.jpg");
        formula1 = loadTexture("formula1.jpg");



        arial40 = new BitmapFont(Gdx.files.internal("Arial40.fnt"),false);

        mapArray = new Image[] {textureToImage(ice), textureToImage(ful)};
        vehicleArray = new Image[] {textureToImage(motorCycle), textureToImage(magicCarpet), textureToImage(car),
                textureToImage(formula1), textureToImage(monsterTruck)};

    }


    public static Image textureToImage (Texture texture) {
        return new Image(texture);
    }

    public static void carHorn(){
        carHorn.play(1);
    }
}
