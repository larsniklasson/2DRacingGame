package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.models.ScreenText;
import edu.chl._2DRacingGame.world.GameRenderer;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.List;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameScreen extends GUIScreen implements Screen {

    private GameWorld world;
    private GameMode gameMode; // TODO this dependency is crazy
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

    public GameScreen(GameWorld world, GameMode mode) {
        this.world = world;
        this.gameMode = mode;
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

    public void addScreenTexts(List<ScreenText> texts) {
        renderer.addScreenTexts(texts);
    }

    @Override
    public void show() {
        startTime = System.nanoTime();
        renderer.retrieveActors();

        // There is a countdown before the race begins, however the world should still be drawn
        // and the vehicles spawned.
        world.update(0);
        renderer.render();
    }

    @Override
    public void render(float delta) {
        renderer.render();
        renderer.draw();
        renderer.act(delta);

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
                gameMode.start();
            }
            spriteBatch.end();
        } else {
            world.update(delta);
        }

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        renderer.dispose();
        ctdwnOne.dispose();
        ctdwnTwo.dispose();
        ctdwnThree.dispose();
        ctdwnGo.dispose();
        spriteBatch.dispose();
    }
}
