package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import edu.chl._2DRacingGame.world.GameRenderer;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private Boolean gameStart;

    private float elapsedTime;
    private long startTime;
    private int centerWidth;
    private int centerHeight;

    private Texture ctdwnOne;
    private Texture ctdwnTwo;
    private Texture ctdwnThree;
    private Texture ctdwnGo;

    private SpriteBatch spriteBatch;

    public GameScreen(){
        world = new GameWorld();
        renderer = new GameRenderer(world);

        gameStart = true;

        ctdwnOne = new Texture("one.png");
        ctdwnTwo = new Texture("two.png");
        ctdwnThree = new Texture("three.png");
        ctdwnGo = new Texture("go.png");

        centerWidth = Gdx.graphics.getWidth()/2;
        centerHeight = Gdx.graphics.getHeight()/2;

        spriteBatch = new SpriteBatch();

    }

    @Override
    public void show() {

        startTime = System.nanoTime();

    }

    @Override
    public void render(float delta) {

        world.update(delta);
        renderer.render();


        if(gameStart) {
            elapsedTime = (System.nanoTime() - startTime) / 1000000000.0f;
            spriteBatch.begin();

            if (elapsedTime < 1f) {
                spriteBatch.draw(ctdwnThree, centerWidth - 178/2, centerHeight - 248/2);
            } else if (elapsedTime < 2f) {
                spriteBatch.draw(ctdwnTwo, centerWidth - 178/2, centerHeight - 248/2);
            } else if (elapsedTime < 3f) {
                spriteBatch.draw(ctdwnOne, centerWidth - 178/2, centerHeight - 248/2);
            } else if (elapsedTime < 4f) {
                spriteBatch.draw(ctdwnGo, centerWidth - 393/2, centerHeight - 248/2);
            } else if (elapsedTime < 5f) {
                gameStart = false;
            }
            spriteBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //renderer.dispose();
    }
}
