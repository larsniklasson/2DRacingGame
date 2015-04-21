package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * Created by Lasse on 2015-04-21.
 */
public class GameRenderer {
    private GameWorld gameWorld;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Box2DDebugRenderer debugRenderer;

    public GameRenderer(GameWorld world){
        this.gameWorld = world;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();


    }
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameWorld.getb2World().step(Gdx.graphics.getDeltaTime(), 3, 3);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.end();

        camera.update();
        debugRenderer.render(gameWorld.getb2World(), camera.combined.cpy().scale(gameWorld.PIXELS_PER_METER, gameWorld.PIXELS_PER_METER, 0));


    }
}
