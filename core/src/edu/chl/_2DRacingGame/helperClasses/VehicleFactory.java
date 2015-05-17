package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.gameObjects.*;

import java.util.Random;

/**
 * @author Daniel Sunnerberg
 */
public class VehicleFactory {

    private static String[] vehicles = {"car", "formulaonecar", "magiccarpet", "motorcycle", "monstertruck"};

    public static Vehicle createRandomVehicle(World world){
        String s = vehicles[new Random().nextInt(vehicles.length)];
        return createVehicle(s, world);
    }

    public static Vehicle createVehicle(String type, World world) {
        switch (type.toLowerCase()) {
            case "car":
                return new Car(world);
            case "formulaonecar":
                return new FormulaOneCar(world);
            case "magiccarpet":
                return new MagicCarpet(world);
            case "motorcycle":
                return new MotorCycle(world);
            case "monstertruck":
                return new MonsterTruck(world);
            default:
                throw new IllegalArgumentException("Found no matching vehicle.");
        }
    }

}
