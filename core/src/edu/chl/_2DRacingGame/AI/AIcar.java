package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Kethupflaskaa on 2015-05-08.
 */
public class AICar implements Steerable<Vector2>{

    private Vector2 position;
    private Vector2 linearVelocity;
    float angularVelocity;
    float maxAngularAcceleration;
    float maxLinearSpeed;
    float maxLinearAcceleration;
    float maxAngularSpeed;
    float orientation;
    boolean tagged;
    float boundingRadius;
    SteeringBehavior<Vector2> steeringBehavior;
    private SteeringAcceleration<Vector2> steeringAcceleration;

    public AICar(){

        this.position = new Vector2();
        this.linearVelocity = new Vector2();
        steeringAcceleration = new SteeringAcceleration<>(new Vector2());
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return outVector.setAngleRad(angle);
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior){
        this.steeringBehavior = steeringBehavior;
    }

    public void update(){
        steeringBehavior.calculateSteering(steeringAcceleration);
    }



}
