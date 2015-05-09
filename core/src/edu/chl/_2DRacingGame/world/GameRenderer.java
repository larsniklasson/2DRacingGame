package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScreenText;

import java.util.List;

/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameRenderer extends Stage {

    boolean debug = true;

    private final GameWorld gameWorld;

    private final SpriteBatch batch;
    private final BitmapFont font;

    private final OrthographicCamera camera;
    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer tiledMapRenderer;

    public GameRenderer(GameWorld world) {
        this.gameWorld = world;
        font = new BitmapFont();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(world.getTiledMap());

        setupActors();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    private void setupActors() {
        addActor(gameWorld.getPlayer().getVehicle().getActor());

        // TODO solve this in a prettier way
        if (gameWorld instanceof MultiplayerGameWorld) {
            List<Player> opponents = ((MultiplayerGameWorld) gameWorld).getOpponents();
            for (Player opponent : opponents) {
                addActor(opponent.getVehicle().getActor());
            }
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            debug = !debug;
        }

        batch.setProjectionMatrix(camera.combined);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        if(debug) debugRenderer.render(gameWorld.getb2World(), camera.combined.cpy().scale(GameWorld.PIXELS_PER_METER, GameWorld.PIXELS_PER_METER, 0));

        batch.begin();
        Vehicle vehicle = gameWorld.getPlayer().getVehicle();
        if(vehicle.getSprites() != null){
            for(Sprite s : vehicle.getSprites()){
                if(s != null){
                    s.draw(batch);
                }
            }
        }

        drawScreenTexts();

        batch.end();
    }

    private void drawScreenTexts() {
        gameWorld.getGameMode().syncTexts();
        for (ScreenText text : gameWorld.getGameMode().getScreenTexts()) {
            font.setColor(text.getColor());
            font.draw(batch, text.getText(), text.getX(), text.getY());
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
