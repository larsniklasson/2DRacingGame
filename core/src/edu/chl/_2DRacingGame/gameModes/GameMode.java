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

    private final List<GameListener> listeners = new ArrayList<>();

    public GameMode(GameListener listener) {
        addListener(listener);
    }

    /**
     * Starts the race.
     */
    public void start() {
        stopWatch.start();
    }

    /**
     * Pauses the race.
     */
    public void pause() {
        if (stopWatch.isStarted()) {
            stopWatch.suspend();
        }
    }

    /**
     * Resumes the race.
     */
    public void resume() {
        if (stopWatch.isSuspended()) {
            stopWatch.resume();
        }
    }

    /**
     * Adds the specified text to the text-queue which are drawn to the screen.
     *
     * @param text Text to be queued
     */
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

    /**
     * Returns a comparator which specifies how scores are compared in the
     * specific game mode.
     *
     * @return Score comparator for the specified game mode
     */
    public abstract Comparator<Double> getScoreComparator();

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public List<GameListener> getListeners() {
        return listeners;
    }
}
