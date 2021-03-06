package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.models.ScoreList;


/**
 * A class which creates a screen to show the race result and options for new game or exit
 * @author Anton Ingvarsson
 */
public class SinglePlayerFinishedScreen extends GUIScreen {


    final private ScoreList scoreList;
    final private double playerScore;
    final private SinglePlayerFinishedScreenListener listener;

    /**
     *
     * @param scoreList     list of previous scores
     * @param playerScore   the players score for the race
     * @param listener      Listener to be notified when related events occur
     */
    public SinglePlayerFinishedScreen(ScoreList scoreList, double playerScore, SinglePlayerFinishedScreenListener listener) {
        this.scoreList = scoreList;
        this.playerScore = playerScore;
        this.listener = listener;
    }

    @Override
    public void show() {
        Table table = new Table();
        Label raceFinished = new Label("Race Finished!", skin, "arial40");
        Label curerntHighscore = new Label("Current highscore: " + String.valueOf(scoreList.getHighScore()),skin,"default");
        Label playerTime = new Label("Your time: " + String.valueOf(playerScore),skin,"default");
        Label newHighscore = new Label("New HighScore!", skin, "default");
        TextButton restartRace = new TextButton("Restart Race", skin, "default");
        TextButton exit = new TextButton("Exit", skin, "default");
        TextButton mainMenu = new TextButton("Main Menu", skin, "default");

        restartRace.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.restartRace();
            }
        });

        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayMainMenu();
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.exitGame();
            }
        });

        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(raceFinished).padBottom(30);
        table.row();
        if(scoreList.isHighScore(playerScore))
            table.add(newHighscore).padBottom(20);
        else table.add(curerntHighscore).padBottom(20);
        table.row();
        table.add(playerTime).padBottom(20);
        table.row();
        table.add(restartRace).padBottom(30).width(200f).height(30f);
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
