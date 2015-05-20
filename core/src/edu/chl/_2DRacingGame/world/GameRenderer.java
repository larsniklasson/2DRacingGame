package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.scenes.scene2d.Stage;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.gameObjects.Drawable;
import edu.chl._2DRacingGame.gameObjects.OurVehicle;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScreenText;

/**
 * Created by Lars Niklasson on 2015-04-21.
 *
 * Class responsible for drawing the game to the screen.
 */
public class GameRenderer extends Stage {

    private boolean showDebug = true;
    private boolean showWayPoints = true;

    private final GameWorld gameWorld;
    private final GameMode gameMode;

    private final SpriteBatch batch;
    private final BitmapFont font;

    private final OrthographicCamera camera;
    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer tiledMapRenderer;


    public GameRenderer(GameWorld world, GameMode gameMode) {
        this.gameWorld = world;
        this.gameMode = gameMode;
        font = new BitmapFont();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(world.getTiledMap());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            showDebug = !showDebug;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            showWayPoints = !showWayPoints;
        }

        batch.setProjectionMatrix(camera.combined);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        if(showDebug) debugRenderer.render(gameWorld.getb2World(), camera.combined.cpy().scale(GameWorld.PIXELS_PER_METER, GameWorld.PIXELS_PER_METER, 0));
        
        
        if(showWayPoints)drawWayPoints();

        batch.begin();
        for (Player player : gameWorld.getPlayers()) {
            Vehicle vehicle = player.getVehicle();
            if(!(vehicle instanceof Drawable)){
                continue;  //nothing to draw
            }

            if(player.isControlledLocally()){
                ((Drawable)vehicle).draw(batch);

            } else {
                //drawing opponents vehicle in multiplayer

                if(vehicle instanceof OurVehicle){

                    //A bit of a hack to change front wheel sprites and not actually the tire-bodies.
                    ((OurVehicle)vehicle).MP_draw(batch);
                } else {
                    //is never called - all vehicle-implementations extend OurVehicle (as of 2015-05-17)

                    ((Drawable)vehicle).draw(batch);

                }

            }





        }

        drawScreenTexts();

        batch.end();
    }

    private void drawWayPoints() {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (Vector2 v2 : gameWorld.getWayPoints()) {

            sr.circle(v2.x*GameWorld.PIXELS_PER_METER, v2.y*GameWorld.PIXELS_PER_METER, 10);
        }

        sr.end();

    }

    private void drawScreenTexts() {
        gameMode.syncTexts();
        for (ScreenText text : gameMode.getScreenTexts()) {
            font.setColor(text.getColor());
            font.draw(batch, text.getText(), text.getX(), text.getY());
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void retrieveActors() {
        for (Player player : gameWorld.getPlayers()) {
            addActor(player.getActor());
        }
    }
}
