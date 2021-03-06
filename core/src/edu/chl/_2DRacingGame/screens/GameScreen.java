package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.chl._2DRacingGame.models.ScreenText;
import edu.chl._2DRacingGame.game.GameRenderer;
import edu.chl._2DRacingGame.game.GameWorld;

import java.util.List;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameScreen extends GUIScreen implements Screen {


    private boolean paused = false;
    private final GameWorld world;
    private GameRenderer renderer;
    private Stage pauseStage;

    private Boolean gameStart;


    private float elapsedTime;
    private long startTime;
    private int centerWidth;
    private int centerHeight;

    private Texture ctdwnOne;
    private Texture ctdwnTwo;
    private Texture ctdwnThree;
    private Texture ctdwnGo;

    private final SpriteBatch spriteBatch;
    private final GameScreenListener listener;


    public GameScreen(GameWorld world, GameScreenListener listener) {


        this.world = world;
        this.listener = listener;
        renderer = new GameRenderer(world);

        pauseStage =  new Stage();
        Gdx.input.setInputProcessor(pauseStage);

        pauseStage.addActor(new PauseScreen(new PauseScreenListener() {
            @Override
            public void resumeRace() {
                pauseGame();

            }

            @Override
            public void displayMainMenu() {
                listener.displayMainMenu();

            }
        }).getPausedScreen());
        gameStart = true;

        ctdwnOne = new Texture("countdown/one.png");
        ctdwnTwo = new Texture("countdown/two.png");
        ctdwnThree = new Texture("countdown/three.png");
        ctdwnGo = new Texture("countdown/go.png");

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
        // There is a countdown before the race begins, however the game should still be drawn
        // and the vehicles spawned.
        world.update(0);
        renderer.render();
    }

    @Override
    public void render(float delta) {
        if(listener.isPauseable() && Gdx.input.isKeyJustPressed(Input.Keys.P)){
            pauseGame();

        }
        if(!paused) {
            renderer.render();
            renderer.draw();
            renderer.act(delta);

            if (gameStart) {
                elapsedTime = (System.nanoTime() - startTime) / 1000000000.0f;
                spriteBatch.begin();

                if (elapsedTime < 1f) {
                    spriteBatch.draw(ctdwnThree, centerWidth - 178 / 2, centerHeight - 248 / 2);
                } else if (elapsedTime < 2f) {
                    spriteBatch.draw(ctdwnTwo, centerWidth - 178 / 2, centerHeight - 248 / 2);
                } else if (elapsedTime < 3f) {
                    spriteBatch.draw(ctdwnOne, centerWidth - 178 / 2, centerHeight - 248 / 2);
                } else if (elapsedTime < 4f) {
                    spriteBatch.draw(ctdwnGo, centerWidth - 393 / 2, centerHeight - 248 / 2);
                } else if (elapsedTime < 5f) {
                    gameStart = false;
                    listener.resume();
                }
                spriteBatch.end();
            } else {
                world.update(delta);
            }
        } else {
            pauseStage.act();
            pauseStage.draw();
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

    private void pauseGame(){
        if(!gameStart){
            paused = !paused;
            if(paused){
                listener.pause();
            } else {
                listener.resume();
            }
        }
    }

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
