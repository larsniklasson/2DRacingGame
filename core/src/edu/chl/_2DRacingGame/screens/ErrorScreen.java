package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.controllers.MainMenuDisplayer;

/**
 * @author Daniel Sunnerberg
 */
public class ErrorScreen extends GUIScreen {

    private final String errorTitle;
    private final String errorMessage;
    private final MainMenuDisplayer listener;

    public ErrorScreen(String errorTitle, String errorMessage, MainMenuDisplayer listener) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();

        Table table = new Table(skin);

        Label errorTitleLabel = new Label(errorTitle, skin, "arial40");
        errorTitleLabel.setColor(Color.BLACK);
        table.add(errorTitleLabel);

        Label errorMessageLabel = new Label(errorMessage, skin);
        errorMessageLabel.setColor(Color.BLACK);
        table.row();
        table.add(errorMessageLabel).left();

        TextButton displayMainMenuButton = new TextButton("Return to main menu", skin);
        displayMainMenuButton.setWidth(200f);
        displayMainMenuButton.setHeight(20f);
        displayMainMenuButton.setColor(Color.GREEN);
        displayMainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.displayMainMenu();
            }
        });
        table.row();
        table.add(displayMainMenuButton).left();

        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(table);
    }
}
