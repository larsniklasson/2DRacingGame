package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.controllers.OptionsScreenListener;

/**
 * Created by Anton on 2015-05-08 revised by Victor Christoffersson 2015-05-22
 */
public class OptionsMenuScreen extends GUIScreen {

    private OptionsScreenListener listener;

    private Table table;

    private Label soundLabel;
    private Label options;

    private TextButton backButton;
    private TextButton applyButton;

    private Slider soundSlider;

    private CheckBox fullscreenCheckBox;

    public OptionsMenuScreen(OptionsScreenListener listener) {
        this.listener = listener;

        table = new Table();
    }

    @Override
    public void show() {
        //table.debug();

        create();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayStartMenu();
            }
        });

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.applyNewOptions(soundSlider.getValue(), fullscreenCheckBox.isChecked());
            }
        });

        table.add(options).colspan(2);
        table.row();
        table.add(soundLabel).colspan(2);
        table.row();
        table.add(soundSlider).colspan(2);
        table.row();
        table.add(fullscreenCheckBox).colspan(2);
        table.row();
        table.add(backButton).padTop(50);
        table.add(applyButton).padTop(50);//.width(200f).height(30f);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    public void create(){
        options = new Label("Options", skin, "arial40");

        soundLabel = new Label("Sound:",skin);
        soundSlider = new Slider(0, 100, 1, false, skin);
        soundSlider.setValue(50);

        fullscreenCheckBox = new CheckBox("Fullscreen", skin);

        backButton = new TextButton("Back", skin, "default");

        applyButton = new TextButton("Apply", skin);
    }

    @Override
    public void hide() {dispose();}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    public Table getTable() {
        return table;
    }

}
