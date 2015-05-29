package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl._2DRacingGame.models.Player;
import edu.chl._2DRacingGame.models.RaceResult;
import edu.chl._2DRacingGame.models.ScoreBoard;

/**
 * Screen which displays a scoreboard when a multiplayer race has been finished.
 *
 * @author Daniel Sunnerberg
 */
public class MultiplayerRaceFinishedScreen extends GUIScreen {

    private static final String WIN_MESSAGE = "Congratulations, you won the race!";
    private static final String LOOSE_MESSAGE = "You reached the goal with position %s/%s.";

    private final ScoreBoard scoreBoard;
    private final Player clientPlayer;

    private Label boardLabel;

    private final MainMenuDisplayer listener;

    /**
     * Creates a new screen with specified arguments.
     *
     * @param clientPlayer our clients player
     * @param scoreBoard scoreboard which holds the race's scores
     * @param listener listener who will handle button-clicks etc.
     */
    public MultiplayerRaceFinishedScreen(Player clientPlayer, ScoreBoard scoreBoard, MainMenuDisplayer listener) {
        this.scoreBoard = scoreBoard;
        this.clientPlayer = clientPlayer;
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();
        Table table = new Table();

        String titleMessage;
        if (scoreBoard.isWinner(clientPlayer)) {
            titleMessage = WIN_MESSAGE;
        } else {
            titleMessage = String.format(LOOSE_MESSAGE, scoreBoard.getPosition(clientPlayer), scoreBoard.getResults().size());
        }
        Label titleLabel = new Label(titleMessage, skin, "arial40");
        titleLabel.setColor(Color.BLACK);
        table.add(titleLabel).row();

        boardLabel = new Label(null, skin);
        boardLabel.setColor(Color.BLACK);
        table.add(boardLabel).left();

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

        String entryFormat = "%s. %s finished in %s seconds\n";

        int position = 1;
        StringBuilder boardLabelBuilder = new StringBuilder();
        for (RaceResult entry : scoreBoard.getResults()) {
            String name = entry.getPlayer().getUserName();
            String timeLabel = entry.getTime() == null ? "-" : String.valueOf(entry.getTime());
            String format = String.format(entryFormat, position, name, timeLabel);
            boardLabelBuilder.append(format);

            position++;
        }
        boardLabel.setText(boardLabelBuilder.toString());
    }

}
