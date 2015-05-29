package edu.chl._2DRacingGame.gameModes;

import com.google.common.base.Stopwatch;
import edu.chl._2DRacingGame.models.ScreenText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Basis for all GameModes, which specifies when a race is considered to be complete.
 *
 * @author Daniel Sunnerberg
 */
public abstract class GameMode implements LapListener {

    private final List<ScreenText> screenTexts = new ArrayList<>();
    private final Stopwatch stopWatch = Stopwatch.createUnstarted();

    private final List<RaceListener> listeners = new ArrayList<>();

    /**
     * Creates a new GameMode instance, with the specified listener.
     * @param listener listener who wishes to receive related events
     */
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
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }
    }

    /**
     * Resumes the race.
     */
    public void resume() {
        if (! stopWatch.isRunning()) {
            start();
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
     * @return all texts which the mode wants to print on the screen
     */
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

    /**
     * Adds a new listener who wishes to receive game mode events
     * @param listener new listener
     */
    public void addListener(RaceListener listener) {
        listeners.add(listener);
    }

    /**
     * @return all game mode event listeners
     */
    public List<RaceListener> getListeners() {
        return listeners;
    }

    /**
     * @return an unique string for the specific mode
     */
    public abstract String toString();
}
