package edu.chl._2DRacingGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import edu.chl._2DRacingGame._2DRacingGame;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen extends ScreenAdapter {
    _2DRacingGame game;
    Rectangle playBounds;
    Rectangle exitBounds;
    Vector3 touchPoint;
    SpriteBatch batcher;
    Texture mainMenuStart;
    Texture mainMenuExit;
    Texture mainMenuBackground;
    Texture mainMenuText;

    public MainMenuScreen (_2DRacingGame game) {
        this.game = game;

        mainMenuBackground = loadTexture("mainMenuBackground.jpg");
        mainMenuText = loadTexture("mainMenuText.png");
        batcher = new SpriteBatch();
        playBounds = new Rectangle(590, 200, 100, 40);
        exitBounds = new Rectangle(590, 280, 100, 40);
        touchPoint = new Vector3();

    }

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
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
        batcher.draw(mainMenuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batcher.draw(mainMenuText,mainMenuText.getWidth()/2, Gdx.graphics.getHeight() -mainMenuText.getHeight());
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