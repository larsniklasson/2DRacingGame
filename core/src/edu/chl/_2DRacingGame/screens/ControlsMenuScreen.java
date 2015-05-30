package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * Displays the control of the game
 *
 * @author Victor Christoffersson
 */
public class ControlsMenuScreen extends GUIScreen {

    private final OptionsScreenListener listener;

    private Table table;

    private Label controlsLabel;

    private TextButton backButton;

    /**
     * Creates an instance of the screen
     *
     * @param listener listener that can display main manu
     */
    public ControlsMenuScreen(OptionsScreenListener listener) {
        this.listener = listener;

        table = new Table();
    }
    /**
     * Creates the tables and all it's components, also adds the table to the stage.
     *
     * Sets the input processor so that the tables components becomes clickable.
     */
    @Override
    public void show() {
        create();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayStartMenu();
            }
        });

        table.add(controlsLabel).colspan(2).padBottom(50);
        table.row();
        table.add(createLabelTable()).left().padRight(20);
        table.add(createKeyTable());
        table.row();
        table.add(backButton).padTop(50).colspan(2);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private void create(){
        controlsLabel = new Label("Controls", skin, "arial40");
        backButton = new TextButton("Back", skin, "default");
    }

    private Table createKeyTable(){
        Table keyTable = keyTable = new Table();

        Label forwardKey = new Label("Up Arrow", skin);
        Label back_reverseKey = new Label("Down Arrow", skin);
        Label turn_leftKey = new Label("Left Arriw", skin);
        Label turn_rightKey = new Label("Right Arrow", skin);
        Label pauseKey = new Label("P", skin);

        keyTable.add(forwardKey).left();
        keyTable.row();
        keyTable.add(back_reverseKey).left();
        keyTable.row();
        keyTable.add(turn_leftKey).left();
        keyTable.row();
        keyTable.add(turn_rightKey).left();
        keyTable.row();
        keyTable.add(pauseKey).left();

        return keyTable;
    }

    private Table createLabelTable(){
        Table labelTable = new Table();

        Label forward = new Label("Forward:", skin);
        Label back_reverse = new Label("Back/reverse:", skin);
        Label turn_left = new Label("Turn left:", skin);
        Label turn_right = new Label("Turn right:", skin);
        Label pause = new Label("Pause:", skin);

        labelTable.add(forward).left();
        labelTable.row();
        labelTable.add(back_reverse).left();
        labelTable.row();
        labelTable.add(turn_left).left();
        labelTable.row();
        labelTable.add(turn_right).left();
        labelTable.row();
        labelTable.add(pause).left();

        return labelTable;
    }

    /**
     * Disposes the screen
     */
    @Override
    public void hide() {dispose();}

    /**
     * Disposes the stage and the skin
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    /**
     * Draws the screen
     *
     * @param delta time between two updates
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    /**
     * @return The table
     */
    public Table getTable() {
        return table;
    }

}
