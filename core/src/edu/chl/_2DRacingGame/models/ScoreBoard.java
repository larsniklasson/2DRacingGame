package edu.chl._2DRacingGame.models;

import java.util.*;
import java.util.stream.Stream;

/**
 * Represents a simple score board which keeps track of when players finished a race and at which position/time.
 *
 * @author Daniel Sunnerberg
 */
public class ScoreBoard {

    /**
     * The map will always be sorted so that the player with the lowest time (float) has the lowest index.
     */
    private Map<Player, Double> scoreBoard = new HashMap<>();

    public void playerFinished(Player player, Double time) {
        scoreBoard.put(player, time);
        scoreBoard = ScoreBoard.sortByValue(scoreBoard);
    }

    /**
     * Sorts a map by its values.
     * Source: Carter Page (http://stackoverflow.com/a/2581754)
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue()))
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    public Map<Player, Double> getFinishedPlayers() {
        Map<Player, Double> copy = new HashMap<>();
        for (Map.Entry<Player, Double> entry : scoreBoard.entrySet()) {
            Double value = entry.getValue() == Double.MAX_VALUE ? null : entry.getValue();
            copy.put(entry.getKey(), value);
        }

        return copy;
    }

    public boolean isWinner(Player player) {
        return scoreBoard.keySet().iterator().next().equals(player);
    }

    /**
     * Ensures that the following players are shown in the scoreboard before they have reached
     * the goal.
     *
     * @param players
     */
    public void trackPlayers(List<Player> players) {
        for (Player player : players) {
            // The sort-method doesn't like null values, use
            // MAX_VALUE as a stupid replacement.
            scoreBoard.put(player, Double.MAX_VALUE);
        }
    }
}
