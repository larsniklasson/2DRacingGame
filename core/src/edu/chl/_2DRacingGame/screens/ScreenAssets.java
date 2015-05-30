package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import edu.chl._2DRacingGame.map.GameMap;

import java.util.ArrayList;

/**
 * A class which
 * @author Anton Ingvarsson
 */
public class ScreenAssets {

    public static ArrayList<Image> mapArray;
    public static ArrayList <Image> vehicleArray;

    /**
     *
     * @param file the path of the file
     * @return  A texture of the file
     */
    private static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
/**
 * Initialize the Arrays and adds the Images for Car and Map selection
 * */
    public static void load(){
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

    }

    private static Image createImage(Texture texture) {
        return new Image(texture);
    }
}


