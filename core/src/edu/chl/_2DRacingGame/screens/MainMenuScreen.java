package edu.chl._2DRacingGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame._2DRacingGame;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen extends ScreenAdapter {
    private _2DRacingGame game;
    private Rectangle playBounds;
    private Rectangle exitBounds;
    private Vector3 touchPoint;
    private SpriteBatch batcher;


    public MainMenuScreen (_2DRacingGame game) {
        this.game = game;
        batcher = new SpriteBatch();
        playBounds = new Rectangle(590, 200, 100, 40);
        exitBounds = new Rectangle(590, 280, 100, 40);
        touchPoint = new Vector3();

    }

    public void update () {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new GameScreen());
                return;
            }
            if(exitBounds.contains(touchPoint.x, touchPoint.y)){
                Gdx.app.exit();
                return;
            }

        }
    }

    public void draw () {
        batcher.enableBlending();
        batcher.begin();
        batcher.draw(Assets.mainMenuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batcher.draw(Assets.mainMenuText,Assets.mainMenuText.getWidth()/2, Gdx.graphics.getHeight() -Assets.mainMenuText.getHeight());
        batcher.end();
    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }

    @Override
    public void pause () {

    }
}