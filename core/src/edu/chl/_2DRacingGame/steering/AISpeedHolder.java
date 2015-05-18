package edu.chl._2DRacingGame.steering;

/**
 * Created by Lars Niklasson on 2015-05-17.
 */
public class AISpeedHolder {

    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;

    public AISpeedHolder(float maxLinearSpeed, float maxLinearAcceleration, float maxAngularSpeed, float maxAngularAcceleration) {
        this.maxLinearSpeed = maxLinearSpeed;
        this.maxLinearAcceleration = maxLinearAcceleration;
        this.maxAngularSpeed = maxAngularSpeed;
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    public static AISpeedHolder getStandardEasySpeed(){
        return new AISpeedHolder(5,1200,1000,1200);
    }

    public static AISpeedHolder getStandardMediumSpeed(){
        return new AISpeedHolder(10,2000,1000,1200);
    }

    public static AISpeedHolder getStandardHardSpeed(){

        return new AISpeedHolder(15,2000,1000,1200);
    }
}
