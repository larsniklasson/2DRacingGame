package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * A class which creates menu screen with options for car, maps, difficulty, laps and number of opponents.
 * @author Anton Ingvarsson
 */
public class SinglePlayerMenuScreen extends GUIScreen {

    private SelectBox<Integer> laps;
    private SelectBox<Integer> numberOfOpponents;
    private SelectBox<String> difficulty;
    private final ScrollTable mapSelector;
    private final ScrollTable vehicleSelector;
    private final Table mainTable;
    private final SetUpListener listener;

    /**
     *
     * @param listener Listener to be notified when related events occur
     */
    public SinglePlayerMenuScreen(SetUpListener listener) {
        this.listener = listener;
        mainTable = new Table();
        mapSelector = new ScrollTable(ScreenAssets.mapArray, "map");
        vehicleSelector = new ScrollTable(ScreenAssets.vehicleArray, "vehicle");
    }


    @Override
    public void show() {
        create();
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates a table with the selection for laps, number of opponents and difficulty.
     * @return the table
     */
    private Table selectTable(){
        Table t = new Table();
        t.bottom();
        final Label lapsLabel = new Label("Number of laps:",skin,"default");
        final Label oppLabel = new Label("Number of opponents:",skin,"default");
        final Label difficultyLabel = new Label ("Select a difficulty:", skin, "default");
        numberOfOpponents = new SelectBox<>(skin);
        laps = new SelectBox<>(skin);
        difficulty = new SelectBox<>(skin);
        laps.setItems(1, 2, 3);
        numberOfOpponents.setItems(0, 1, 2, 3, 4, 5, 6);
        difficulty.setItems("Easy", "Medium","Hard");
        t.add(lapsLabel);
        t.row();
        t.add(laps).padBottom(15);
        t.row();
        t.add(oppLabel);
        t.row();
        t.add(numberOfOpponents).padBottom(15);
        t.row();
        t.add(difficultyLabel);
        t.row();
        t.add(difficulty);
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
                listener.displayMainMenu();
            }
        });

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.startRace(vehicleSelector.getImageName(), mapSelector.getImageName(), difficulty.getSelected(),
                        stringToInt(laps.getSelected().toString()), stringToInt(numberOfOpponents.getSelected().toString()));

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