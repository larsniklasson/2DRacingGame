package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.ScoreBoard;

import java.util.Map;

/**
 * @author Daniel Sunnerberg
 */
public class MultiplayerRaceFinishedScreen extends GUIScreen {

    private static final String WIN_MESSAGE = "Congratulations, you won the race!";
    private static final String LOOSE_MESSAGE = "Sorry, you did not win the race."; // TODO position

    private final ScoreBoard scoreBoard;
    private final Player clientPlayer;

    private Table table;
    private Label boardLabel;

    private final RaceSummaryListener listener;

    public MultiplayerRaceFinishedScreen(Player clientPlayer, ScoreBoard scoreBoard, RaceSummaryListener listener) {
        this.scoreBoard = scoreBoard;
        this.clientPlayer = clientPlayer;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();
        table = new Table();

        String titleMessage = scoreBoard.isWinner(clientPlayer) ? WIN_MESSAGE : LOOSE_MESSAGE;
        Label titleLabel = new Label(titleMessage, skin); // TODO size
        titleLabel.setColor(Color.BLACK);
        table.add(titleLabel).row();

        boardLabel = new Label(null, skin);
        boardLabel.setColor(Color.BLACK);
        table.add(boardLabel);

        TextButton displayMainMenuButton = new TextButton("Return to main menu", skin, "default");
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

    @Override
    public void render(float delta) {
        super.render(delta);

        String entryFormat = "%s. %s finished in %s\n";

        int position = 1;
        StringBuilder boardLabelBuilder = new StringBuilder();
        for (Map.Entry<Player, Double> entry : scoreBoard.getFinishedPlayers().entrySet()) {
            String name = entry.getKey().getUserName();
            String timeLabel = entry.getValue() == null ? "-" : String.valueOf(entry.getValue());
            String format = String.format(entryFormat, position, name, timeLabel);
            boardLabelBuilder.append(format);

            position++;
        }
        boardLabel.setText(boardLabelBuilder.toString());
    }

}
