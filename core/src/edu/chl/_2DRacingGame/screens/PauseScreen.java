package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * @author Anton Ingvarsson
 */
public class PauseScreen extends GUIScreen {

    private Window window;
    final private PauseScreenListener listener;

    public PauseScreen(PauseScreenListener listener) {
        this.listener = listener;
        show();
    }

    @Override
    public void show() {
        final TextButton resumeButton = new TextButton("Resume", skin, "default");
        final TextButton quitRaceButton = new TextButton("Quit Race", skin, "default");
        final Label pausedLabel = new Label("Paused", skin, "arial40");

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.resumeRace();
            }
        });



        quitRaceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayMainMenu();

            }
        });

        Table table = new Table(skin);
        window = new Window("", skin);
        table.add(pausedLabel);
        table.row();
        table.add(resumeButton).padTop(50).width(200f).height(30f);
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

