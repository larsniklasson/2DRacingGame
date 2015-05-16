package edu.chl._2DRacingGame.models;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * @author Daniel Sunnerberg
 */
public class RaceResult implements Comparable<RaceResult> {

    private final Player player;

    /**
     * Time it took to finish the race.
     */
    private Double time;

    public RaceResult(Player player, Double time) {
        this.player = player;
        this.time = time;
    }

    public RaceResult(Player player) {
        this(player, null);
    }

    public Player getPlayer() {
        return player;
    }

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

    public void updateTime(Double time) {
        this.time = time;
    }
}
