package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Victor Christoffersson on 2015-05-19.
 */
public class MultipPlayerMenuScreen extends GUIScreen {

    private MultiPlayerMenuListener listener;

    private Table table;


    private TextButton startButton;
    private TextButton backButton;

    private Label multiPlayerLabel;

    public MultipPlayerMenuScreen(MultiPlayerMenuListener listener) {

        this.listener = listener;

        table = new Table();

    }

    @Override
    public void show() {
        create();

        stage.addActor(table);
        table.setFillParent(true);

        Gdx.input.setInputProcessor(stage);

    }

    public void create(){
        table.setDebug(true);

        multiPlayerLabel = new Label("Multiplayer", skin, "arial40");
        table.add(multiPlayerLabel).colspan(2);
        table.row();

        backButton = new TextButton("back", skin);
        table.add(backButton);

        startButton = new TextButton("Find race", skin);
        table.add(startButton);

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
