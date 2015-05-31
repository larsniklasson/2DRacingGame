package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * GUI screen which let's the user decide which settings to use in a multiplayer race.
 *
 * @author Victor Christoffersson
 */
public class MultiPlayerMenuScreen extends GUIScreen {

    private final MultiPlayerMenuListener listener;

    private final Table mainTable;
    private final Table carouselTable;
    private final Table buttonTable;
    private final ScrollTable2 vehicleTable;
    private final ScrollTable2 mapTable;

    /**
     * Creates an instance of the screen.
     * Creates instances of ScrollTable2 which will be added to this table.
     *
     * @param listener will handle all events
     */
    public MultiPlayerMenuScreen(MultiPlayerMenuListener listener) {

        this.listener = listener;

        vehicleTable = new ScrollTable2(ScreenAssets.vehicleArray, "Vehicle");
        mapTable = new ScrollTable2(ScreenAssets.mapArray, "Map");
        mainTable = new Table();
        carouselTable = new Table();
        buttonTable = new Table();

    }

    /**
     * Creates the tables and all it's components, also adds the table to the stage.
     *
     * Sets the input processor so that the tables components becomes clickable.
     */
    @Override
    public void show() {
        create();

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);

    }

    private void create(){
        Label multiPlayerLabel = new Label("Multiplayer", skin, "arial40");
        mainTable.add(multiPlayerLabel).colspan(2);
        mainTable.row();

        carouselTable.add(vehicleTable.getTable());
        carouselTable.row();
        carouselTable.add(mapTable.getTable());
        carouselTable.setSize(Gdx.graphics.getWidth(), 200);

        mainTable.add(carouselTable);
        mainTable.row();

        TextButton backButton = new TextButton("back", skin);
        buttonTable.add(backButton).left();

        TextButton startButton = new TextButton("Find race", skin);
        buttonTable.add(startButton).right();

        mainTable.add(buttonTable);

        startButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                listener.findOpponents(vehicleTable.getImageName(), mapTable.getImageName());
            }
        });

        backButton.addListener((new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.displayMainMenu();
            }
        }));
        mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    /**
     * Disposes the stage
     */
    @Override
    public void hide() {
        stage.dispose();
    }

    /**
     * Draws the stage on the screen
     *
     * @param delta time between two frames
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    /**
     * Disposes the stage and skin
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}