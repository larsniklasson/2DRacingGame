package edu.chl._2DRacingGame.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Victor Christoffersson on 2015-05-17.
 */
public class WayPoints {

    private Pathfollower pathfollower;
    private ShapeRenderer shapeRenderer;

    public WayPoints(Vector2 startingPosition){

        Vector2 startingPosition1 = startingPosition;

        shapeRenderer = new ShapeRenderer();

        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("carbody.png")));
        sprite.setOrigin(0, 0);

        pathfollower = new Pathfollower(sprite, createPath());
        pathfollower.setPosition(startingPosition.x, startingPosition.y);

    }

    public Array<Vector2> createPath(){
        Array<Vector2> path = new Array<Vector2>();
        for(int i = 0; i < MathUtils.random(5, 20); i++){
            path.add(new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight())));
        }
        return path;
    }

    public Pathfollower getPathfollower(){
        return pathfollower;
    }

    public void update(SpriteBatch batch){

            pathfollower.draw(batch);

    }

    public void drawPath(){

        Vector2 prev = pathfollower.getPath().first();

        for(Vector2 wayPoint : pathfollower.getPath()) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(prev, wayPoint);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(wayPoint.x, wayPoint.y, 5);
            shapeRenderer.end();

            prev = wayPoint;
        }
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(new Vector2(pathfollower.getX(), pathfollower.getY()), pathfollower.getPath().get(pathfollower.getWayPoint()));
        shapeRenderer.end();
    }




}

