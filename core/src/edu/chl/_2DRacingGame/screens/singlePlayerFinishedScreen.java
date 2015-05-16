package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame._2DRacingGame;

import javax.xml.soap.Text;

/**
 * Created by Anton on 2015-05-12.
 */
public class SinglePlayerFinishedScreen extends GUIScreen {

    private _2DRacingGame game;
    private String playerTime;


    public SinglePlayerFinishedScreen(_2DRacingGame game) {
        this.game = game;

    }

    @Override
    public void show() {
        Table table = new Table();
        Label raceFinished = new Label("Race Finished!", skin, "arial40");
        Label curerntHighscore = new Label("",skin,"default");
        Label playerTime = new Label("",skin,"default");
        Label newHighscore = new Label("New HighScore!", skin, "default");
        TextButton restartRace = new TextButton("Restart Race", skin, "default");
        TextButton exit = new TextButton("Exit", skin, "default");
        TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        TextButton newRace = new TextButton("New Race", skin, "default");

        newRace.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayerMenu(game));
            }
        });

        restartRace.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.restartRace();
            }
        });

        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.displayMainMenu();
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.debug();
        table.add(raceFinished).padBottom(50);
        table.row();
        table.add(restartRace).padBottom(30).width(200f).height(30f);
        table.row();
        table.add(newRace).padBottom(30).width(200f).height(30f);
        table.row();
        table.add(mainMenu).padBottom(30).width(200f).height(30f);
        table.row();
        table.add(exit).padBottom(30).width(200f).height(30f);
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

    }

    @Override
    public void dispose() {

    }
}
