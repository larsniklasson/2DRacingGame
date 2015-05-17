package edu.chl._2DRacingGame.models;

import java.util.*;
import java.util.stream.Stream;

/**
 * Represents a simple score board which keeps track of when players finished a race and at which position/time.
 *
 * @author Daniel Sunnerberg
 */
public class ScoreBoard {

    private TreeSet<RaceResult> scoreBoard = new TreeSet<>();

    public void addResult(Player player, Double time) {
        for (RaceResult result : scoreBoard) {
            if (result.getPlayer().equals(player)) {
                // Remove and then add the result to ensure set order
                scoreBoard.remove(result);
                result.updateTime(time);
                scoreBoard.add(result);
                return;
            }
        }

        scoreBoard.add(new RaceResult(player, time));
    }

    public Set<RaceResult> getResults() {
        return scoreBoard;
    }

    public boolean isWinner(Player player) {
        if (scoreBoard.isEmpty()) {
            return false;
        }

        RaceResult leader = scoreBoard.iterator().next();
        return leader.getPlayer().equals(player) && leader.getTime() != null;
    }

    /**
     * Ensures that the following players are shown in the scoreboard before they have reached
     * the goal.
     *
     * @param players
     */
    public void trackPlayers(List<Player> players) {
        for (Player player : players) {
            scoreBoard.add(new RaceResult(player, null));
        }
    }

    /**
     * Gets the race position of the specified player.
     *
     * @param player
     * @return
     */
    public int getPosition(Player player) {
        int position = 1;
        for (RaceResult entry : scoreBoard) {
            if (entry.getPlayer().equals(player)) {
                return position;
            }

            position++;
        }

        throw new IllegalArgumentException("Player has not finished the race or is not  tracked.");
    }
}
