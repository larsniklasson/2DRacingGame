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
}
