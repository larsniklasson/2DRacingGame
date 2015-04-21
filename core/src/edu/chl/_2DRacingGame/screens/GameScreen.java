package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Screen;
import edu.chl._2DRacingGame.world.GameRenderer;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lasse on 2015-04-21.
 */
public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;

    public GameScreen(){
        world = new GameWorld();
        renderer = new GameRenderer(world);



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        world.update(delta);
        renderer.render();

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

    }
}
