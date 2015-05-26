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

    private enum State {GAME_RUNNING, GAME_PAUSED}
    private State state;
    private SpriteBatch spriteBatch;
    private  Vector3 touchPoint;
    private Rectangle resumeBounds;
    private Rectangle exitBounds;

    public GameScreen(GameWorld world, GameMode mode) {
        this.world = world;
        this.gameMode = mode;
        renderer = new GameRenderer(world);

        state = State.GAME_RUNNING;
        touchPoint = new Vector3();
        resumeBounds = new Rectangle(470,140,410,90);
        exitBounds = new Rectangle(470,500,410,90);
        gameStart = true;

        ctdwnOne = new Texture("one.png");
        ctdwnTwo = new Texture("two.png");
        ctdwnThree = new Texture("three.png");
        ctdwnGo = new Texture("go.png");

        centerWidth = Gdx.graphics.getWidth()/2;
        centerHeight = Gdx.graphics.getHeight()/2;

        spriteBatch = new SpriteBatch();
    }

    public void update(float delta){
        switch(state){
            case GAME_RUNNING:
                updateRunning(delta);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            default: System.out.println("Something went wrong!");

        }

    }

    private void updatePaused() {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                this.state = State.GAME_RUNNING;
                gameMode.resume();
                return;
            }
            if(exitBounds.contains(touchPoint.x, touchPoint.y)){
                Gdx.app.exit();
                return;
            }
        }

        /* bortplockad för att programmet läser in ett knaptryck väldigt snabbt vilket gör att den
           byter mellen pause och run flera gånger utan någon precision vart den slutar.
        if (Gdx.input.isKeyPressed(Input.Keys.P)){
            if(this.state == State.GAME_PAUSED)
                this.state = State.GAME_RUNNING;

        }*/
    }

    private void updateRunning(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.P)){
            if(this.state == State.GAME_RUNNING) {
                gameMode.pause();
                this.state = State.GAME_PAUSED;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            Assets.carHorn();

        world.update(delta);
    }

    public void draw(float delta) {

        spriteBatch.begin();
        switch (state) {
            case GAME_RUNNING:
                drawRunning(delta);
                break;
            case GAME_PAUSED:
                drawPaused();
                break;
            default:
                System.out.println("Something went wrong!");
        }
        spriteBatch.end();
    }

    private void drawPaused() {
        //stage.addActor(new PauseScreen().getPausedScreen());
        //stage.draw();
        //spriteBatch.draw(Assets.pauseMenu,centerWidth-Assets.pauseMenu.getWidth()/2 ,0);
    }

    private void drawRunning(float delta) {
        renderer.render();
        renderer.draw();
        renderer.act(delta);
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
        draw(delta);

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
            update(delta);
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
