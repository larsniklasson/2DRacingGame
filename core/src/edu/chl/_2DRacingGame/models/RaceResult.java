package edu.chl._2DRacingGame.models;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * Represents a player's result during a race.
 *
 * @author Daniel Sunnerberg
 */
public class RaceResult implements Comparable<RaceResult> {

    private final Player player;

    /**
     * Time it took to finish the race.
     */
    private Double time;

    /**
     * Creates a new result for the specified player and time.
     *
     * @param player player which drove the race
     * @param time time it took to run the race, or null if not yet complete
     */
    public RaceResult(Player player, Double time) {
        this.player = player;
        this.time = time;
    }

    /**
     * @return the result's player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the time it took to finish the race, or null if not yet complete
     */
    public Double getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceResult that = (RaceResult) o;

        if (!player.equals(that.player)) return false;
        return !(time != null ? !time.equals(that.time) : that.time != null);

    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(RaceResult other) {
        return ComparisonChain.start()
                .compare(this.time, other.time, Ordering.natural().nullsLast())
                .compare(this.getPlayer().getUserName(), other.getPlayer().getUserName())
                .result();

    }

    /**
     * @param time new time
     */
    public void updateTime(Double time) {
        this.time = time;
    }
}
