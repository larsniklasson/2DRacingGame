package edu.chl._2DRacingGame.vehicle.factory;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import edu.chl._2DRacingGame.models.ISteeringSystem;
import edu.chl._2DRacingGame.models.Vehicle;
import edu.chl._2DRacingGame.vehicle.Difficulty;
import edu.chl._2DRacingGame.vehicle.gameVehicles.*;
import edu.chl._2DRacingGame.vehicle.steering.*;

import java.util.Random;

/**
 * Factory used to create vehicles.
 * Used to easily create different kinds of vehicles with the right steering-system set automatically.
 *
 * @author Daniel Sunnerberg
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
     * @param world The Box2D-game the vehicle will be created in
     * @param vehicleType The type of vehicle.
     *
     * @return The created vehicle.
     */
    public static Vehicle createVehicle(World world, String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case CAR:
                return new Car(world);
            case FORMULA_ONE_CAR:
                return new FormulaOneCar(world);
            case MAGIC_CARPET:
                return new MagicCarpet(world);
            case MOTORCYCLE:
                return new MotorCycle(world);
            case MONSTER_TRUCK:
                return new MonsterTruck(world);
            case RANDOM_VEHICLE:
                return createVehicle(world, getRandomVehicleType());
            default:
                throw new IllegalArgumentException("Found no matching vehicle.");
        }
    }


    /**
     * Creates a bot-vehicle, controlled by Artificial Intelligence (AI).
     * For the type of vehicle use constants defined in this class, or the name of the class
     * in all lower case letters.
     *
     * NOTE: returns a WheeledVehicle since the AI-system requires a body and wheels.
     *
     * @param world The Box2D-game the vehicle will be created in
     * @param vehicleType The type of vehicle.
     * @param difficulty The difficulty-setting of the AI.
     * @param wayPoints The points the vehicle's AI-system will follow
     * @return The created AI-controlled vehicle.
     */
    public static WheeledVehicle createAIVehicle(World world, String vehicleType, Difficulty difficulty, Array<Vector2> wayPoints){
        WheeledVehicle wv = (WheeledVehicle) createVehicle(world, vehicleType);
        WheeledAISteeringEntity steeringEntity = new WheeledAISteeringEntity(wv, wv.getAISpeeds(difficulty));


        wv.setSteeringSystem(new WayPointSystem(steeringEntity, wayPoints));
        return wv;
    }


    /**
     * Creates a user-controllable vehicle. Different keys to control the vehicle based on what player-number
     * specified.
     * For the type of vehicle use constants defined in this class, or the name of the class
     * in all lower case letters.
     *
     * @param world The Box2D-game the vehicle will be created in
     * @param vehicleType The type of vehicle.
     * @param playerNumber The player-number. (Player 1, Player 2, etc.)
     * @return The created user-controllable vehicle.
     */
    public static WheeledVehicle createPlayerVehicle(World world, String vehicleType, int playerNumber){

        WheeledVehicle wv = (WheeledVehicle) createVehicle(world, vehicleType);
        SteeringInputListener sl = getSteeringInputListener(playerNumber);

        ISteeringSystem ss;
        if(vehicleType.equals(MAGIC_CARPET)){
            ss = new FlyingSteeringSystem(wv, sl);
        } else {
            ss = new WheelSteeringSystem(wv, sl);
        }

        wv.setSteeringSystem(ss);
        return wv;
    }

}
