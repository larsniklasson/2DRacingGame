package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Victor Christoffersson on 2015-05-17.
 */
public class Pathfollower extends Sprite{

    private Vector2 velocity = new Vector2();
    private float speed = 300;

    private Array<Vector2> path;
    private float closeness = 3;
    private int wayPoint = 0;

    public Pathfollower(Sprite sprite, Array<Vector2> path){
        super(sprite);

        this.path = path;

    }

    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta){
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

    public boolean isAtWayPoint(){
        return path.get(wayPoint).x - getX() <= speed / closeness * Gdx.graphics.getDeltaTime() && path.get(wayPoint).y - getY() <= speed / closeness * Gdx.graphics.getDeltaTime();
    }

    public int getWayPoint(){
        return wayPoint;
    }
}

