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
 * Makes pathfollowers and their set path to follow
 *
 * @author Victor Christoffersson
 */
public class WayPoints {

    private final Array<Pathfollower> pathfollowers;
    private final ShapeRenderer shapeRenderer;

    /**
     * Creates the pathfollowers and a random path on the screen for them to follow
     */
    public WayPoints(){

        shapeRenderer = new ShapeRenderer();
        pathfollowers = new Array<>();

        Sprite sprite = new Sprite(new Texture("nyancat.png"));
        sprite.setOrigin(0, 0);

        for(int i = 0; i <= 50; i++) {
            Pathfollower pathfollower = new Pathfollower(sprite, createPath());
            pathfollower.setPosition(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
            pathfollowers.add(pathfollower);
        }
    }

    private Array<Vector2> createPath(){
        Array<Vector2> path = new Array<Vector2>();
        for(int i = 0; i < MathUtils.random(5, 20); i++){
            path.add(new Vector2(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight())));
        }
        return path;
    }

    /**
     * Draws all the pathfollower on a batch
     *
     * @param batch batch where pathfollower will be drawn
     */
    public void draw(SpriteBatch batch){

        for(Pathfollower pathfollower : pathfollowers) {
            pathfollower.draw(batch);
        }
    }

    /**
     * Draws the path for all pathfollowers
     */
    public void drawPath() {

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Pathfollower pathfollower : pathfollowers) {
            Vector2 prev = pathfollower.getPath().first();
            for (Vector2 wayPoint : pathfollower.getPath()) {
                shapeRenderer.line(prev, wayPoint);
                prev = wayPoint;
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Pathfollower pathfollower : pathfollowers){
            for (Vector2 wayPoint : pathfollower.getPath()) {
                shapeRenderer.circle(wayPoint.x, wayPoint.y, 5);
            }
        }
        shapeRenderer.end();

        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Pathfollower pathfollower : pathfollowers) {
            shapeRenderer.line(new Vector2(pathfollower.getX(), pathfollower.getY()), pathfollower.getPath().get(pathfollower.getWayPoint()));
        }
        shapeRenderer.end();

    }
}

