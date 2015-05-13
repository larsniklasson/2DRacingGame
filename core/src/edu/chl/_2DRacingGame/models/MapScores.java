package edu.chl._2DRacingGame.models;

import edu.chl._2DRacingGame.gameModes.GameMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Stores the users stores for the different map/mode combinations.
 * Hence, a score on map A running mode X will not be listed along with a score on map A running mode Y and so on.
 *
 * IMPORTANT NOTE: You probably want to try to get eventual scores from earlier game sessions,
 * for that, see the MapScoresPersistor class.
 *
 * @see MapScoresPersistor
 * @author Daniel Sunnerberg
 */
public class MapScores {

    private final GameMap map;
    private transient final GameMode mode;

    private final Comparator<Double> highscoreComparator;

    /**
     * This list will always be sorted after the highscoreComparator. Should normally be a TreeSet, but then its objects
     * would need to be wrapped since it's the GameMode who decides the ordering.
     */
    private final List<Double> scores;

    public MapScores(GameMap map, GameMode mode) {
        this(map, mode, new ArrayList<>());
    }

    public MapScores(GameMap map, GameMode mode, List<Double> scores) {
        this.map = map;
        this.mode = mode;
        this.scores = scores;
        highscoreComparator = mode.getScoreComparator();
    }

    /**
     * Adds a score for the actual map/mode combination.
     *
     * @param score Score to be added
     */
    public void addScore(double score) {
        scores.add(score);
        scores.sort(highscoreComparator);
    }

    /**
     * Gets the highest score for the actual map/mode combination.
     *
     * @return highest score or null if no scores are recorded
     */
    public Double getHighScore() {
        if (scores.isEmpty()) {
            return null;
        }
        return scores.get(0);
    }

    /**
     * Returns whether the specified score is the highest so far on the actual map/mode combination.
     *
     * @param other score to compare against
     * @return whether the score is the highest so far
     */
    public boolean isHighScore(Double other) {
        return scores.isEmpty() || highscoreComparator.compare(getHighScore(), other) >= 0;
    }

    /**
     * Returns all stored scores.
     *
     * @return all stored scores
     */
    public List<Double> getScores() {
        return scores;
    }
}
