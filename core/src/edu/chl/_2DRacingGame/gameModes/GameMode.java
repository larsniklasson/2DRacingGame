package edu.chl._2DRacingGame.gameModes;

import edu.chl._2DRacingGame.models.ScreenText;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public abstract class GameMode implements LapListener {

    private ArrayList<ScreenText> screenTexts = new ArrayList<>();
    private StopWatch stopWatch = new StopWatch();

    protected void addScreenText(ScreenText text) {
        screenTexts.add(text);
    }

    public void syncTexts() {}

    public List<ScreenText> getScreenTexts() {
        return screenTexts;
    }

    protected StopWatch getStopWatch() {
        return stopWatch;
    }
}
