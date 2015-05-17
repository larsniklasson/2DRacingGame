package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame._2DRacingGame;

/**
 * Created by Anton on 2015-05-08.
 */
public class OptionsMenuScreen extends GUIScreen {

    private _2DRacingGame game;

    public OptionsMenuScreen(_2DRacingGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        final TextButton backButton = new TextButton("Back", skin, "default");
        final Label options = new Label("Options", skin, "arial40");

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table table = new Table();
        table.add(options);
        table.row();
        table.add(backButton).padTop(50).width(200f).height(30f);
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        table.debug();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {dispose();}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
