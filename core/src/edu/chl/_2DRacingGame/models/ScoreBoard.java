package edu.chl._2DRacingGame.models;

import java.util.*;

/**
 * Represents a simple score board which keeps track of how long it took to finish a task and at which position/time.
 * TODO should be generic and not coupled to Player.
 *
 * @author Daniel Sunnerberg
 */
public class ScoreBoard {

    private final Set<RaceResult> scoreBoard = new TreeSet<>();

    /**
     * Adds a result to the scoreboard. If a player already has a posted score, the time will be updated.
     *
     * @param player owner of the score
     * @param time time it took to do the task
     */
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

    /**
     * @return all added results
     */
    public Set<RaceResult> getResults() {
        return scoreBoard;
    }

    /**
     * @param player player to be checked
     * @return whether the specified player is the winner
     */
    public boolean isWinner(Player player) {
        if (scoreBoard.isEmpty()) {
            return false;
        }

        RaceResult leader = scoreBoard.iterator().next();
        return leader.getPlayer().equals(player) && leader.getTime() != null;
    }

    /**
     * @return the winner, or null if none exist
     */
    public Player getWinner() {
        if (scoreBoard.isEmpty()) {
            return null;
        }

        return scoreBoard.iterator().next().getPlayer();
    }

    /**
     * Ensures that the following players are shown in the scoreboard before they have reached
     * the goal.
     *
     * @param players players to be tracked
     */
    public void trackPlayers(List<Player> players) {
        for (Player player : players) {
            scoreBoard.add(new RaceResult(player, null));
        }
    }

    /**
     * Gets position of the specified player, sorted by time
     *
     * @param player player to get position of
     * @return the players position
     * @throws IllegalArgumentException if the player haven't finished the task
     */
    public int getPosition(Player player) {
        int position = 1;
        for (RaceResult entry : scoreBoard) {
            if (entry.getPlayer().equals(player)) {
                return position;
            }

            position++;
        }

        throw new IllegalArgumentException("Player has not finished the race or is not tracked.");
    }
}
