package edu.chl._2DRacingGame.helperClasses;


import edu.chl._2DRacingGame.gameObjects.*;
import edu.chl._2DRacingGame.steering.*;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.Random;

/**
 * Factory used to create vehicles.
 * Used to easily create different kinds of vehicles with the right steering-system set automatically.
 *
 * @author Daniel Sunnerberg
 *
 * revised by Lars Niklasson 2015-05-19, added alot of functionality
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

    private static final String[] vehicles = {CAR, FORMULA_ONE_CAR, MOTORCYCLE, MONSTER_TRUCK, MAGIC_CARPET};


    private static String getRandomVehicleType(){
        return vehicles[new Random().nextInt(vehicles.length)];
    }


    /**
     * Creates a vehicle in the specified GameWorld.
     * For the type of vehicle use constants defined in this class, or the name of the class
     * in all lower case letters.
     * 
     * NOTE: does not have a steering-system.
     * @param gameWorld the GameWorld the vehicle will be created in.
     * @param vehicleType The type of vehicle.
     *
     * @return The created vehicle.
     */
    public static Vehicle createVehicle(GameWorld gameWorld, String vehicleType) {
        switch (vehicleType.toLowerCase()) {
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


    /**
     * Creates a bot-vehicle, controlled by Artificial Intelligence (AI).
     * For the type of vehicle use constants defined in this class, or the name of the class
     * in all lower case letters.
     *
     * NOTE: returns an OurVehicle since the AI-system requires a body and wheels.
     *
     * @param gameWorld The GameWorld the vehicle will be created in.
     * @param vehicleType The type of vehicle.
     * @param difficulty The difficulty-setting of the AI.
     * @return The created AI-controlled vehicle.
     */
    public static OurVehicle createAIVehicle(GameWorld gameWorld, String vehicleType, Difficulty difficulty){
        OurVehicle ov = (OurVehicle) createVehicle(gameWorld, vehicleType);
        ov.setSteeringSystem(new WayPointSystem(new WheeledAISteeringEntity(ov, ov.getAISpeeds(difficulty)), gameWorld.getWayPoints()));
        return ov;
    }


    /**
     * Creates a user-controllable vehicle. Different keys to control the vehicle based on what player-number
     * specified.
     * For the type of vehicle use constants defined in this class, or the name of the class
     * in all lower case letters.
     *
     * @param gameWorld The GameWorld the vehicle will be created in.
     * @param vehicleType The type of vehicle.
     * @param playerNumber The player-number. (Player 1, Player 2, etc.)
     * @return The created user-controllable vehicle.
     */
    public static OurVehicle createPlayerVehicle(GameWorld gameWorld, String vehicleType, int playerNumber){

        OurVehicle ov = (OurVehicle) createVehicle(gameWorld, vehicleType);
        SteeringInputListener sl = getSteeringInputListener(playerNumber);

        ISteeringSystem ss;
        if(vehicleType.equals(MAGIC_CARPET)){
            ss = new FlyingSteeringSystem(ov, sl);
        } else {
            ss = new TireSteeringSystem(ov, sl);
        }

        ov.setSteeringSystem(ss);
        return ov;
    }




}
