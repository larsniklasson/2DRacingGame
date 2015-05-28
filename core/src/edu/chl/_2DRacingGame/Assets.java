package edu.chl._2DRacingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import edu.chl._2DRacingGame.models.GameMap;

import java.util.ArrayList;


/**
 * @author Anton Ingvarsson
 */
public class Assets {

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

    //magic carpet

    public static Texture magicCarpetBody;

    public static Texture nyanCat;

    public static ArrayList <Image> mapArray;
    public static ArrayList <Image> vehicleArray;

    private static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load(){

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

        //-------------------


        //--------Images for Vehicle and Map Selection
        mapArray = new ArrayList<>();
        for (GameMap map : GameMap.values()) {
            Image mapOverview = createImage(loadTexture(map.getOverviewImagePath()));
            mapOverview.setName(map.name());
            mapArray.add(mapOverview);
        }

        String[] vehicleNames = new String [] {"motorcycle.jpg", "car.png", "monstertruck.jpg", "formula1car.jpg", "magiccarpet.jpg", "random.jpg"};
        vehicleArray = new ArrayList<>();
        for(int i = 0; i<vehicleNames.length;i++){
            System.out.println(i);
            vehicleArray.add(createImage(loadTexture("VehicleSelectorImages/" + vehicleNames[i])));
            //vehicleArray.get(i).setName(vehicleNames[i]);
            switch (vehicleNames[i]) {
                case "motorcycle.jpg":
                    vehicleArray.get(i).setName("motorcycle");
                    break;
                case "car.png":
                    vehicleArray.get(i).setName("car");
                    break;
                case "monstertruck.jpg":
                    vehicleArray.get(i).setName("monstertruck");
                    break;
                case "formula1car.jpg":
                    vehicleArray.get(i).setName("formulaonecar");
                    break;
                case "magiccarpet.jpg":
                    vehicleArray.get(i).setName("magiccarpet");
                    break;
                case "random.jpg":
                    vehicleArray.get(i).setName("random_vehicle");
                    break;

            }
        }
        nyanCat = loadTexture("nyancat.png");
    }

    private static Image createImage(Texture texture) {
        return new Image(texture);
    }
}
