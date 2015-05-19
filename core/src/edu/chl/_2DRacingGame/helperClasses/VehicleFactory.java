package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.steering.*;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.Random;

/**
 * @author Daniel Sunnerberg
 *
 * revised by Lars Niklasson 2015-05-19
 */
public class VehicleFactory {

    public static final String CAR = "car";
    public static final String FORMULA_ONE_CAR = "formulaonecar";
    public static final String MOTORCYCLE = "motorcycle";
    public static final String MAGIC_CARPET = "magiccarpet";
    public static final String MONSTER_TRUCK = "monstertruck";
    public static final String RANDOM_VEHICLE = "random_vehicle";


    private static SteeringInputListener getSteeringInputListener(int playerNumber){
        switch (playerNumber){
            case 1:
                return new PlayerOneInputListener();
            case 2:
                return new PlayerTwoInputListener();
            default:
                throw new IllegalArgumentException("Couldn't find an inputlistener for that number.");
        }

    }

    private static String[] vehicles = {CAR, FORMULA_ONE_CAR, MOTORCYCLE, MONSTER_TRUCK, MAGIC_CARPET};


    private static String getRandomVehicleType(){
        return vehicles[new Random().nextInt(vehicles.length)];
    }



    public static Vehicle createVehicle(GameWorld gameWorld, String vehicleName) {
        switch (vehicleName.toLowerCase()) {
            case CAR:
                return new Car(gameWorld.getb2World());
            case FORMULA_ONE_CAR:
                return new FormulaOneCar(gameWorld.getb2World());
            case MAGIC_CARPET:
                return new MagicCarpet(gameWorld.getb2World());
            case MOTORCYCLE:
                return new MotorCycle(gameWorld.getb2World());
            case MONSTER_TRUCK:
                return new MonsterTruck(gameWorld.getb2World());
            case RANDOM_VEHICLE:
                return createVehicle(gameWorld, getRandomVehicleType());
            default:
                throw new IllegalArgumentException("Found no matching vehicle.");
        }
    }

    public static OurVehicle createAIVehicle(GameWorld gameWorld, String vehicleName, Difficulty difficulty){
        OurVehicle ov = (OurVehicle) createVehicle(gameWorld, vehicleName);
        ov.setSteeringSystem(new WayPointSystem(ov, gameWorld.wayPoints, difficulty));
        return ov;
    }



    public static OurVehicle createPlayerVehicle(GameWorld gameWorld, String vehicleName, int playerNumber){

        OurVehicle ov = (OurVehicle) createVehicle(gameWorld, vehicleName);
        SteeringInputListener sl = getSteeringInputListener(playerNumber);

        SteeringSystem ss;
        if(vehicleName.equals(MAGIC_CARPET)){
            ss = new FlyingSteeringSystem(ov, sl);
        } else {
            ss = new TireSteeringSystem(ov, sl);
        }

        ov.setSteeringSystem(ss);
        return ov;
    }




}
