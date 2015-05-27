package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author Victor Christoffersson
 */
public class Pathfollower extends Sprite{

    private final Vector2 velocity = new Vector2();
    private final float speed = 300;

    private final Array<Vector2> path;
    private int wayPoint = 0;

    public Pathfollower(Sprite sprite, Array<Vector2> path){
        super(sprite);

        this.path = path;

    }

    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

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

    public Array<Vector2> getPath(){
        return path;
    }

    private boolean isAtWayPoint(){
        return Math.abs(path.get(wayPoint).x - getX()) <= speed * Gdx.graphics.getDeltaTime() && Math.abs(path.get(wayPoint).y - getY()) <= speed * Gdx.graphics.getDeltaTime();
    }

    public int getWayPoint(){
        return wayPoint;
    }
}

