package edu.chl._2DRacingGame.gameModes;

import edu.chl._2DRacingGame.models.ScreenText;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Requests that all needed ScreenTexts are updated. Likely called before
     * drawing them on the screen.
     */
    public void syncTexts() {}

    public List<ScreenText> getScreenTexts() {
        return screenTexts;
    }

    protected StopWatch getStopWatch() {
        return stopWatch;
    }

    public abstract Comparator<Double> getHighscoreComparator();
}
