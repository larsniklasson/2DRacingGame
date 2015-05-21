package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Victor Christoffersson on 2015-05-21.
 */
public class SearchingForPlayerScreen extends GUIScreen {

    private Table table;

    private TextButton cancelButton;

    public SearchingForPlayerScreen(){
        table = new Table();
    }

    @Override
    public void show() {
        create();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    public void create(){

        cancelButton = new TextButton("cancel", skin);
        table.add(cancelButton);

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
