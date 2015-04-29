package edu.chl._2DRacingGame.gameModes;

import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.models.ScreenText;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Daniel Sunnerberg
 */
public class TimeTrial extends GameMode {

    private int currentLap = 1;
    private final int lapGoal = 2; // TODO inject from menu

    private final ScreenText currentLapText;

    public TimeTrial() {
        currentLapText = new ScreenText(new Vector2(1200, 670));
        syncTexts();
    }

    private void syncTexts() {
        currentLapText.setText(String.format("Lap %d/%d", currentLap, lapGoal));
    }

    @Override
    public void lap() {
        currentLap++;
        syncTexts();
    }

    @Override
    public List<ScreenText> getScreenTexts() {
        ArrayList<ScreenText> texts = new ArrayList<>();
        texts.add(currentLapText);
        return texts;
    }
}
