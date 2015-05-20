package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame._2DRacingGame;
import javafx.scene.control.RadioButton;


/**
 * Created by Anton on 2015-05-08.
 */
public class SinglePlayerMenuScreen extends GUIScreen {



    private SelectBox<Integer> laps;
    private SelectBox<Integer> numberOfOpponents;

    private ScrollTable mapSelector;
    private ScrollTable vehicleSelector;
    private Table mainTable;
    private MainMenuListener listener;

    public SinglePlayerMenuScreen(MainMenuListener listener) {
        this.listener = listener;
        mainTable = new Table();
        mapSelector = new ScrollTable(Assets.mapArray, "map");
        vehicleSelector = new ScrollTable(Assets.vehicleArray, "vehicle");
    }


    @Override
    public void show() {
        create();
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    private Table selectTable(){
        Table t = new Table();
        t.bottom();
        final Label lapsLabel = new Label("Number of laps:",skin,"default");
        final Label oppLabel = new Label("Number of opponents:",skin,"default");
        numberOfOpponents = new SelectBox<>(skin);
        laps = new SelectBox<>(skin);
        laps.setItems(1,2,3);
        numberOfOpponents.setItems(1,2,3,4,5,6);
        t.add(lapsLabel);
        t.row();
        t.add(laps).padBottom(30);
        t.row();
        t.add(oppLabel);
        t.row();
        t.add(numberOfOpponents);
        t.bottom();

        return t;
    }

    private void create() {
        final TextButton startGameButton = new TextButton("Start Game!", skin, "default");
        final Label singlePlayer = new Label("Single Player", skin, "arial40");
        final TextButton backButton = new TextButton("back", skin, "default");
        Table t = selectTable();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayStartMenu();
            }
        });

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Number of Opponents: " + stringToInt(numberOfOpponents.getSelected().toString()));
                System .out.println("Number of Laps: " + stringToInt(laps.getSelected().toString()));
                System.out.println("Vehicle chosen: " + vehicleSelector.getImageName());
                System.out.println("Map chosen: " + mapSelector.getImageName());
                listener.startSinglePlayerRace();

            }
        });

        mainTable.add(singlePlayer).colspan(4).padBottom(50);
        mainTable.row();
        mainTable.add(mapSelector.getTable());
        mainTable.add(vehicleSelector.getTable());
        mainTable.add(t).top();
        mainTable.row();
        mainTable.add(backButton).left().padTop(50).width(200f).height(30f);
        mainTable.add(startGameButton).colspan(4).right().padTop(50).width(200f).height(30f);
        mainTable.debug();
        mainTable.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    private int stringToInt(String s) {return Integer.parseInt(s);}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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