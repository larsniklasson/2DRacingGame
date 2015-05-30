package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Sprite that can follow a path
 *
 * @author Victor Christoffersson
 */
public class Pathfollower extends Sprite{

    private final Vector2 velocity = new Vector2();
    private final float speed = 300;

    private final Array<Vector2> path;
    private int wayPoint = 0;

    /**
     * Sets the path for this pathfollower
     *
     * @param sprite sprite which can be drawn
     * @param path path which will be followed
     */
    public Pathfollower(Sprite sprite, Array<Vector2> path){
        super(sprite);

        this.path = path;

    }

    /**
     * Draws the pathfollower on the batch
     *
     * @param spriteBatch batch which will be drawn upon
     */
    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    /**
     * Updates pathfollowers to go towards the next waypoint on the path
     * Sets the position and angle
     * Checks if the pathfollower is at a waypoint, and makes it go to next next if it is
     *
     *
     * @param delta time between to updates
     */
    private void update(float delta){
        float angle = (float) Math.atan2(path.get(wayPoint).y - getY(), path.get(wayPoint).x - getX());
        velocity.set((float) Math.cos(angle) * speed ,(float) Math.sin(angle) * speed);

        setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
        setRotation(angle * MathUtils.radiansToDegrees);

        if(isAtWayPoint()){
            if(wayPoint + 1 >= path.size){
                wayPoint = 0;
            } else {
                wayPoint++;
            }

        }
    }

    /**
     * returns the pathfollowers path
     *
     * @return path which is being followed
     */
    public Array<Vector2> getPath(){
        return path;
    }

    private boolean isAtWayPoint(){
        return Math.abs(path.get(wayPoint).x - getX()) <= speed * Gdx.graphics.getDeltaTime() && Math.abs(path.get(wayPoint).y - getY()) <= speed * Gdx.graphics.getDeltaTime();
    }

    /**
     * Returns the index for which waypoint pathfollower is going towards on the path
     *
     * @return index for which waypoint pathfollower is going towards on the path
     */
    public int getWayPoint(){
        return wayPoint;
    }
}

