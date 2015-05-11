package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame._2DRacingGame;


/**
 * Created by Anton on 2015-05-08.
 */
public class MultiPlayerMenu implements Screen {

    private _2DRacingGame game;
    private Skin skin;
    private Stage stage;


    public MultiPlayerMenu(_2DRacingGame game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
    }

    @Override
    public void show() {
        final TextButton startGameButton = new TextButton("start game", skin, "default");
        final TextButton backButton = new TextButton("Back", skin, "default");
        final Label multiPlayer = new Label("MultiPlayer", skin, "arial40");

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }
        });
        Table table = new Table();
        table.add(multiPlayer).colspan(2);
        table.row();
        table.add(backButton).padTop(50).width(200f).height(30f);
        table.add(startGameButton).padTop(50).width(200f).height(30f);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.debug();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();

    }
}
