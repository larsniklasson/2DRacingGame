package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.controllers.PauseScreenListener;


/**
 * Created by Anton on 2015-05-17.
 */
public class PauseScreen extends GUIScreen {

    private Table table;
    private Window window;
    final private PauseScreenListener listener;

    public PauseScreen(PauseScreenListener listener) {
        this.listener = listener;
        show();
    }

    @Override
    public void show() {
        final TextButton resumeButton = new TextButton("Resume", skin, "default");
        final TextButton restartRaceButton = new TextButton("Restart Race", skin, "default");
        final TextButton optionsButton = new TextButton("Options", skin, "default");
        final TextButton quitRaceButton = new TextButton("Quit Race", skin, "default");
        final Label pausedLabel = new Label("Paused", skin, "arial40");

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        restartRaceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        quitRaceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        table = new Table(skin);
        window = new Window("", skin);
        table.add(pausedLabel);
        table.row();
        table.add(resumeButton).padTop(50).width(200f).height(30f);
        table.row();
        table.add(restartRaceButton).padTop(50).width(200f).height(30f);
        table.row();
        table.add(optionsButton).padTop(50).width(200f).height(30f);
        table.row();
        table.add(quitRaceButton).padTop(50).width(200f).height(30f);
        table.row();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.debug();
        window.setResizable(false);
        window.setMovable(false);
        window.setSize(table.getWidth() / 3f, table.getHeight() / 1.5f);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2 ,stage.getHeight() / 2 - window.getWidth() / 2);

        window.add(table);
    }

    public Window getPausedScreen(){
        return window;
    }

    @Override
    public void hide() {dispose();}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

