package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * @author Victor Christoffersson
 */
public class SearchingForPlayerScreen extends GUIScreen {

    private final Table table;

    private final MultiPlayerMenuListener listener;

    private TextButton cancelButton;
    private TextButton tryAgainButton;

    private Label infoLabel;

    public SearchingForPlayerScreen(MultiPlayerMenuListener listener){
        table = new Table();

        this.listener = listener;
    }

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
