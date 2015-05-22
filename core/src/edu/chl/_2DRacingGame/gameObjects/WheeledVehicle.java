package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import edu.chl._2DRacingGame.steering.AISpeedHolder;
import edu.chl._2DRacingGame.steering.Difficulty;
import edu.chl._2DRacingGame.steering.WheelSteerable;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation designed for the racing game 2DRacingGame
 * Has an option to add wheels using the Wheel-class. The wheels can be placed anywhere but
 * they will be divided into front wheels and back wheels.
 *
 * Is designed specifically to work with the steering-systems used in 2DRacingGame,
 * WheelSteeringSystem and AISteeringSystem, and for the multi-player mode in said game.
 *
 *
 *@author Lars Niklasson
 */
public abstract class WheeledVehicle extends SingleBodyVehicle implements Drawable, WheelSteerable {


    private Sprite sprite;

    private final List<Wheel> wheels = new ArrayList<>();
    private final List<Vector2> wheelPositions = new ArrayList<>();

    private final List<RevoluteJoint> frontJoints = new ArrayList<>();
    private float maxTurnAngle = 50f;
    private float turnDegreesPerSecond = 1000;

    private float currentFrontWheelAngle = 0;

    //A bit of a hack to get the wheels to turn in multiplayer. Should only be used from multiplayer-classes
    private float MP_FrontWheelAngle = 0;

    private final List<Boolean> isFrontWheelBooleanList = new ArrayList<>();  //maybe not needed but was used for MP before

    /**
     * Creates a vehicle set in the specified Box2D-world.
     * IMPORTANT NOTE: Calling createBody() is necessary to have a functioning vehicle when subclassing.
     *
     * @param world the Box2D-world which the vehicle will be created in.
     */
    public WheeledVehicle(World world) {
        super(world);

    }


    /**
     * Attaches a wheel to the vehicle-body at the specified position.
     * If the front-wheel flag is set, creates a RevoluteJoint which is used to turn the wheel.
     * If not the wheel will not be able to turn.
     *
     * IMPORTANT NOTE: A body must be created before this method is called.
     *
     * @param wheel The wheel that will be attached.
     * @param position The position the wheel will be placed at, relative to the body.
     * @param frontWheel If the wheel is a front-wheel or a back-wheel.
     */
    protected void attachWheel(Wheel wheel, Vector2 position, boolean frontWheel){
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();
        jointDef.bodyB = wheel.getBody();
        jointDef.localAnchorA.set(position);

        if(frontWheel){
            frontJoints.add((RevoluteJoint) world.createJoint(jointDef));

        } else {
            world.createJoint(jointDef);
        }

        wheelPositions.add(position);

        wheels.add(wheel);

        isFrontWheelBooleanList.add(frontWheel);
    }



    @Override
    public void place(Vector2 position, float angle) {
        body.setTransform(position, angle);
        for(int i = 0; i < wheelPositions.size(); i++){
            Wheel w = wheels.get(i);
            Vector2 pos = wheelPositions.get(i);
            w.getBody().setTransform(body.getWorldPoint(pos), angle);
        }
    }

    /**
     * Updates the sprites based on the position of the body and wheels, and draws the
     * sprites using the specified SpriteBatch
     * @param batch The SpriteBatch which is used to draw the sprites of the body and wheels
     */
    @Override
    public void draw(SpriteBatch batch){
        if(sprite == null)return;
        
        SpriteUtils.updateSprite(body, sprite, GameWorld.PIXELS_PER_METER);
        sprite.draw(batch);
        for(Wheel w : wheels){
            w.draw(batch);
        }
    }

    /**
     * Note: Specifically designed for 2DRacingGame's multi-player mode.
     *
     * Is used to update the opponents vehicle.
     * Instead of rotating the wheels and adjust the sprites, only the sprites are rotated. (Not the actual wheels)
     * This is to prevent bugs in multi-player caused by long time between updates.
     *
     * @param batch The SpriteBatch which is used to draw the sprites of the body and wheels
     */
    //hack for drawing opponent in MP. Only the sprites of the frontwheels are rotated to prevent weird bugs.
    public void MP_draw(SpriteBatch batch){
        SpriteUtils.updateSprite(body, sprite, GameWorld.PIXELS_PER_METER);
        sprite.draw(batch);
        for(int i = 0; i < wheels.size(); i ++){
            Wheel w = wheels.get(i);

            if(w.getSprite() == null)continue; //currently only for MagicCarpet but you shouldn't have to set the sprite;

            SpriteUtils.updateSprite(w.getBody(), w.getSprite(), GameWorld.PIXELS_PER_METER);
            if(isFrontWheelBooleanList.get(i)){

                SpriteUtils.rotateSpriteBy(w.getSprite(), MP_FrontWheelAngle);
            }
            w.getSprite().draw(batch);
        }

    }

    /**
     *
     * @return The current Sprite of the vehicle-body.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Set the Sprite of the vehicle-body.
     * @param sprite The Sprite that will be used to draw the vehicles body
     */
    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public float getMaxTurnAngle() {
        return maxTurnAngle;
    }

    /**
     *
     * @param maxTurnAngle The maximum angle which the front-wheels can be turned.
     */
    protected void setMaxTurnAngle(float maxTurnAngle) {
        this.maxTurnAngle = maxTurnAngle;
    }

    @Override
    public float getTurnDegreesPerSecond() {
        return turnDegreesPerSecond;
    }

    /**
     *
     * @param turnDegreesPerSecond The Degrees per Second which the front-wheels can be turned.
     */
    protected void setTurnDegreesPerSecond(float turnDegreesPerSecond) {
        this.turnDegreesPerSecond = turnDegreesPerSecond;
    }

    /**
     *
     * @return The current angle of the frontwheels.
     */
    public float getCurrentFrontWheelAngle() {
        return currentFrontWheelAngle;
    }

    @Override
    public void setCurrentFrontWheelAngle(float currentFrontWheelAngle) {
        this.currentFrontWheelAngle = currentFrontWheelAngle;
    }

    /**
     * Sets the front-wheel angle used to draw the opponents front-wheels in multi-player
     * @param mp_frontWheelAngle The front-wheel angle of the opponent
     */
    public void setMP_FrontWheelAngle(float mp_frontWheelAngle){  //TODO bad variable names
        this.MP_FrontWheelAngle = mp_frontWheelAngle;
    }

    @Override
    public List<RevoluteJoint> getFrontJoints(){
        return frontJoints;
    }

    @Override
    public List<Wheel> getWheels() {
        return wheels;
    }

    /**
     * Note: Is supposed to be used while looping through all the wheels.
     *
     * @return A list indicating if a wheel is a front-wheel or not.
     */
    public List<Boolean> getIsFrontWheelBooleanList(){
        return isFrontWheelBooleanList;
    }

    /**
     * Returns the Maximum speed and acceleration for the AI-version of this vehicle, based on the difficulty specified
     *
     * @param difficulty The difficulty specified
     * @return The speed-holder for the AI-version of this vehicle based on difficulty.
     */
    public AISpeedHolder getAISpeeds(Difficulty difficulty){
        if(difficulty == null){
            return AISpeedHolder.getStandardMediumSpeed();
        }

        switch (difficulty){
            case Easy:
                return getEasySpeeds();

            case Medium:
                return getMediumSpeeds();

            case Hard:
                return getHardSpeeds();


        }
        return null;
    }

    /**
     *
     * @return The speed-holder for the AI-version of this vehicle with easy difficulty.
     */
    public abstract AISpeedHolder getEasySpeeds();

    /**
     *
     * @return The speed-holder for the AI-version of this vehicle with medium difficulty.
     */
    public abstract AISpeedHolder getMediumSpeeds();

    /**
     *
     * @return The speed-holder for the AI-version of this vehicle with hard difficulty.
     */
    public abstract AISpeedHolder getHardSpeeds();


}
