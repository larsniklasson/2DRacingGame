package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import edu.chl._2DRacingGame.controllers.MultiPlayerMenuListener;

/**
 * Screen that displays status when searching for opponents in multiplayer.
 * Gives the user a chance to cancel and retry a new search.
 *
 * @author Victor Christoffersson
 */
public class SearchingForPlayerScreen extends GUIScreen {

    private final Table table;

    private final MultiPlayerMenuListener listener;

    private TextButton cancelButton;
    private TextButton tryAgainButton;

    private Label infoLabel;

    /**
     * Creates the screen and table where all components will be added.
     *
     * @param listener will handle all events
     */
    public SearchingForPlayerScreen(MultiPlayerMenuListener listener){
        table = new Table();

        this.listener = listener;
    }

    /**
     * Creates the tables all it's components, also adds the table to the stage.
     *
     * Sets the input processor so that the tables components becomes clickable.
     */
    @Override
    public void show() {
        create();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    private void create(){
        infoLabel = new Label("Searching for player", skin);
        tryAgainButton = new TextButton("Try again", skin);
        tryAgainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.print("Started new search");
                listener.searchAgain();
            }
        });
        cancelButton = new TextButton("cancel", skin);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.cancelSearch();
            }
        });
        table.add(infoLabel);
        table.row();
        table.add(cancelButton);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Disposes the stage.
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
     * Disposes the stage and the skin.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    /**
     * Gets called when an error has occured and changes the table to display info about that error.
     */
    public void displayErrorInfo(){
        table.reset();
        infoLabel.setText("Failed to start multiplayer game, please check your connection and try again.");
        table.add(infoLabel).colspan(2);
        table.row();
        table.add(cancelButton);
        table.add(tryAgainButton);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.debug();

    }
}
