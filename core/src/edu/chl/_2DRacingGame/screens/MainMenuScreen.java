package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame._2DRacingGame;

/**
 *
 */
public class MainMenuScreen extends GUIScreen {

    private MainMenuListener listener;

    public MainMenuScreen(MainMenuListener listener) {

        this.listener = listener;
    }

    @Override
    public void show() {
        final TextButton singlePlayer = new TextButton("Single Player", skin, "default");
        final TextButton multiPlayer = new TextButton("MultiPlayer", skin, "default");
        final TextButton options = new TextButton("options", skin, "default");
        final TextButton exit = new TextButton("Exit", skin, "default");
        final Label gameTitle = new Label("2D Racing Game!", skin, "arial40");
        final Table table = new Table();


        //Adds Listeners to all buttons
        singlePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displaySinglePlayerMenuScreen();
            }
        });

        multiPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayMultiPlayerMenuScreen();
            }
        });

        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayOptionsScreen();
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.exitGame();
            }
        });

        // colspan(2) +center() gör att elementen centreras när det bara är ett element på raden men table har 2 kolumner

        table.add(gameTitle).padBottom(60).colspan(2).center();
        table.row();
        table.add(singlePlayer).width(400f).height(40f).padRight(50);
        table.add(multiPlayer).width(400f).height(40f);
        table.row();
        table.add(options).width(150f).height(30f).padTop(30).colspan(2).center();
        table.row();
        table.add(exit).width(150f).height(30f).padTop(30).colspan(2).center();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        table.debug(); // ger markeringar, ska tas bort
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

}