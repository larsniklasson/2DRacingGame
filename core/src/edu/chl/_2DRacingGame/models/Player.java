package edu.chl._2DRacingGame.models;

import edu.chl._2DRacingGame.gameObjects.Vehicle;

import java.util.Date;

/**
 * Created by Victor on 2015-05-07.
 */
public class Player {

    /**
     * The player's username must be unique when playing online.
     */
    private final String userName;

    /**
     * Since the Vehicle class contains Box2D-references which are cyclic, the vehicle itself cannot be
     * serialized. Hence storing the type again.
     */
    private String vehicleType;
    private transient Vehicle vehicle;

    /**
     * Whether the player instance is controlled by our client or not.
     */
    private boolean isControlledByClient = false;

    public Player(String userName, Vehicle vehicle) {
        this.userName = userName;
        this.vehicle = vehicle;
    }

    public Player() {
        this(generateUserName(), null);
    }

    private static String generateUserName() {
        return "usr-" + new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleType = vehicle.getClass().getSimpleName();
    }

    public void setIsControlledByClient(boolean isControlledByClient) {
        this.isControlledByClient = isControlledByClient;
    }

    public boolean isControlledByClient() {
        return isControlledByClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return userName.equals(player.userName);

    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    public String getVehicleType() {
        return vehicleType;
    }

}

