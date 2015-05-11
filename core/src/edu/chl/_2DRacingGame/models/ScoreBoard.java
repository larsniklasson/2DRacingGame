package edu.chl._2DRacingGame.models;

import java.util.Comparator;
import java.util.HashMap;
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
    private Map<Player, Float> scoreBoard = new HashMap<>();

    public void addPlayer(Player player, float time) {
        scoreBoard.put(player, time);
        sortScoreBoard();
    }

    private void sortScoreBoard() {
        Map<Player, Float> sortedMap = new HashMap<>();
        Stream<Map.Entry<Player, Float>> stream = scoreBoard.entrySet().stream();
        stream.sorted(Comparator.comparing(Map.Entry::getValue)).forEach(
                e -> sortedMap.put(e.getKey(), e.getValue())
        );
        scoreBoard = sortedMap;
    }

    public boolean isWinner(Player player) {
        return scoreBoard.keySet().iterator().next().equals(player);
    }

}
