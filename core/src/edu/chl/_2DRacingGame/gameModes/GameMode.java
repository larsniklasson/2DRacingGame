package edu.chl._2DRacingGame.gameModes;

import edu.chl._2DRacingGame.models.ScreenText;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public abstract class GameMode implements LapListener {

    private final List<ScreenText> screenTexts = new ArrayList<>();
    private final Stopwatch stopWatch = Stopwatch.createUnstarted();

    private final List<RaceListener> listeners = new ArrayList<>();

    public GameMode(RaceListener listener) {
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
        stopWatch.stop();
    }

    /**
     * Resumes the race.
     */
    public void resume() {
        stopWatch.start();
    }

    /**
     * Adds the specified text to the text-queue which are drawn to the screen.
     *
     * @param text Text to be queued
     */
    protected void addScreenText(ScreenText text) {
        screenTexts.add(text);
    }

    public List<ScreenText> getScreenTexts() {
        return screenTexts;
    }

    protected Stopwatch getStopWatch() {
        return stopWatch;
    }

    /**
     * Returns a comparator which specifies how scores are compared in the
     * specific game mode.
     *
     * @return Score comparator for the specified game mode
     */
    public abstract Comparator<Double> getScoreComparator();

    public void addListener(RaceListener listener) {
        listeners.add(listener);
    }

    public List<RaceListener> getListeners() {
        return listeners;
    }

    public abstract String getModeName();
}
