package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import edu.chl._2DRacingGame.gameObjects.Vehicle;

/**
 * Ports basic Actor usability to Vehicles.
 *
 * IMPORTANT NOTE: Only methods overridden below are implemented and guaranteed to be working.
 * Other Actor-methods may work; many are not realistically applicable to a Vehicle, though.
 *
 * @author Daniel Sunnerberg
 */
public class VehicleActor extends Actor {

    private final Vehicle vehicle;

    public VehicleActor(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    private Vector2 getVehiclePosition() {
        return vehicle.getBody().getTransform().getPosition().cpy();
    }

    @Override
    public float getX(int alignment) {
        return getX();
    }

    @Override
    public float getX() {
        return getVehiclePosition().x;
    }

    @Override
    public float getY(int alignment) {
        return getY();
    }

    @Override
    public float getY() {
        return getVehiclePosition().y;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        Vector2 position = new Vector2(x, getVehiclePosition().y);
        vehicle.place(position, getRotation());
    }



    @Override
    public void setY(float y) {
        super.setY(y);
        Vector2 position = new Vector2(getVehiclePosition().x, y);
        vehicle.place(position, getRotation());
    }

    @Override
    public float getRotation() {
        return vehicle.getBody().getTransform().getRotation();
    }

    @Override
    public void setRotation(float degrees) {
        vehicle.getBody().setTransform(getVehiclePosition(), (float) Math.toDegrees(degrees));
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        vehicle.place(new Vector2(x, y), vehicle.getBody().getAngle());
    }


}
