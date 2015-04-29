package edu.chl._2DRacingGame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.models.ScreenText;


/**
 * Created by Lars Niklasson on 2015-04-21.
 */
public class GameRenderer {
    private GameWorld gameWorld;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture carTexture;

    private Texture tireTexture;
    private Sprite carSprite;
    private Sprite tireSprite;

    private BitmapFont font;



    private Box2DDebugRenderer debugRenderer;
    private TiledMapRenderer tiledMapRenderer;

    public GameRenderer(GameWorld world){
        this.gameWorld = world;

        carTexture = new Texture(Gdx.files.internal("carbody.png"));
        tireTexture = new Texture(Gdx.files.internal("tire.png"));

        carSprite = new Sprite(carTexture);
        tireSprite = new Sprite(tireTexture);

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

        gameWorld.getb2World().step(Gdx.graphics.getDeltaTime(), 3, 3);

        batch.setProjectionMatrix(camera.combined);



        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        debugRenderer.render(gameWorld.getb2World(), camera.combined.cpy().scale(GameWorld.PIXELS_PER_METER, GameWorld.PIXELS_PER_METER, 0));



        batch.begin();

        for(Tire t: gameWorld.getCar().getTires()){
            tireSprite.setPosition((t.getBody().getWorldCenter().x * GameWorld.PIXELS_PER_METER) - tireSprite.getWidth()/2,
                    (t.getBody().getWorldCenter().y * GameWorld.PIXELS_PER_METER) - tireSprite.getHeight()/2);
            tireSprite.setRotation((float) Math.toDegrees(t.getBody().getAngle()));
            tireSprite.draw(batch);
        }

        

        carSprite.setPosition((gameWorld.getCar().body.getWorldCenter().x * GameWorld.PIXELS_PER_METER) - carSprite.getWidth() / 2,
                (gameWorld.getCar().body.getWorldCenter().y * GameWorld.PIXELS_PER_METER) - carSprite.getHeight() / 2);
        carSprite.setRotation((float) Math.toDegrees(gameWorld.getCar().body.getAngle()));


        drawScreenTexts();

        carSprite.draw(batch);

        batch.end();

    }

    private void drawScreenTexts() {
        gameWorld.getGameMode().syncTexts();
        for (ScreenText text : gameWorld.getGameMode().getScreenTexts()) {
            font.setColor(text.getColor());
            font.draw(batch, text.getText(), text.getX(), text.getY());
        }
    }

    public void dispose(){
        batch.dispose();
        carTexture.dispose();
        tireTexture.dispose();
        font.dispose();
    }
}
