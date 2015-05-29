package edu.chl._2DRacingGame.gameObjects.steering;

import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import edu.chl._2DRacingGame.gameObjects.Wheel;

import java.util.List;

/**
 * Interface created for the WheelSteeringSystem.
 *
 *@author Lars Niklasson
 */
public interface WheelSteerable {

    /**
     *
     * @return A List containing the steerable's wheels.
     */
    List<Wheel> getWheels();

    /**
     *
     * @return A List of the Revolute-joints of the front-wheels.
     */
    List<RevoluteJoint> getFrontJoints();

    /**
     *
     * @return The maximum angle which the front-wheels can be turned.
     */
    float getMaxTurnAngle();

    /**
     *
     * @return The Degrees per Second which the front-wheels can be turned.
     */
    float getTurnDegreesPerSecond();


    /**
     * Sets the current angle of the front wheels.
     * NOTE: this does not affect the steering-systems, but is only used for multi-player purposes,
     * due to rather poor design.
     * @param currentFrontWheelAngle The current angle of the frontwheels.
     */
    void setCurrentFrontWheelAngle(float currentFrontWheelAngle);

}
