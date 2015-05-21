package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.gameObjects.Vehicle;

/**
 * Created by Victor Christoffersson on 2015-05-19.
 */
public class MultipPlayerMenuScreen extends GUIScreen {

    private MultiPlayerMenuListener listener;

    private Table mainTable;
    private Table carouselTable;
    private Table buttonTable;
    private ScrollTable2 vehicleTable;
    private ScrollTable2 mapTable;


    private TextButton startButton;
    private TextButton backButton;

    private Label multiPlayerLabel;

    public MultipPlayerMenuScreen(MultiPlayerMenuListener listener) {

        this.listener = listener;

        vehicleTable = new ScrollTable2(Assets.vehicleArray, "Vehicle");
        mapTable = new ScrollTable2(Assets.mapArray, "Map");
        mainTable = new Table();
        carouselTable = new Table();
        buttonTable = new Table();

    }

    @Override
    public void show() {
        create();

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);

    }

    public void create(){
        mainTable.setDebug(true);

        multiPlayerLabel = new Label("Multiplayer", skin, "arial40");
        mainTable.add(multiPlayerLabel).colspan(2);
        mainTable.row();

        carouselTable.add(vehicleTable.getTable());
        carouselTable.row();
        carouselTable.add(mapTable.getTable());
        carouselTable.setSize(Gdx.graphics.getWidth(), 200);

        mainTable.add(carouselTable);
        mainTable.row();

        backButton = new TextButton("back", skin);
        buttonTable.add(backButton).left();

        startButton = new TextButton("Find race", skin);
        buttonTable.add(startButton).right();

        mainTable.add(buttonTable);

        startButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("search for opponent");
                listener.startMultiplayerRace();
            }
        });

        backButton.addListener((new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("back");
                listener.displayMainMenuScreen();
            }
        }));
        mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }


    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}