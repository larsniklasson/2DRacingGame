package edu.chl._2DRacingGame.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        sortScoreBoard();
    }

    private void sortScoreBoard() {
        Map<Player, Double> sortedMap = new HashMap<>();
        Stream<Map.Entry<Player, Double>> stream = scoreBoard.entrySet().stream();
        stream.sorted(Comparator.comparing(Map.Entry::getValue)).forEach(
                e -> sortedMap.put(e.getKey(), e.getValue())
        );
        scoreBoard = sortedMap;
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
