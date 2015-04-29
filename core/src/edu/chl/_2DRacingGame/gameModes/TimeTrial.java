package edu.chl._2DRacingGame.gameModes;

import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.models.ScreenText;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Daniel Sunnerberg
 */
public class TimeTrial extends GameMode {

    private final GameListener listener;

    private int currentLap = 1;
    private final int lapGoal = 1; // TODO inject from menu

    private final ScreenText currentLapText;
    private final ScreenText currentRaceTimeText;

    public TimeTrial(GameListener listener) {
        this.listener = listener;

        currentLapText = new ScreenText(new Vector2(1200, 670));
        addScreenText(currentLapText);
        currentRaceTimeText = new ScreenText(new Vector2(1200, 655));
        addScreenText(currentRaceTimeText);

        syncTexts();
        getStopWatch().start(); // TODO should not start right away
    }

    @Override
    public void syncTexts() {
        currentLapText.setText(String.format("Lap %d/%d", currentLap, lapGoal));
        currentRaceTimeText.setText(getStopWatch().getTime() / 1000d + "s");
    }

    @Override
    public void lap() {
        syncTexts();

        if (currentLap == lapGoal) {
            getStopWatch().stop();
            listener.gameFinished("You drove the track in: " + getStopWatch().getTime() / 1000d + " seconds.");
        }

        currentLap++;
    }
}
