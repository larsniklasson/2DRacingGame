package edu.chl._2DRacingGame.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * A list of scores. The list will always be sorted after the comparator specified upon construction to allow natural
 * iteration. Should normally be a TreeSet, but then its objects would need to be wrapped since it's a separate
 * comparator who decides the ordering.
 *
 * @author Daniel Sunnerberg
 */
public class ScoreList extends ArrayList<Double> {

    private final Comparator<Double> scoreComparator;

    /**
     * Creates a new instance with no existing scores. Scores added to this instance will be sorted after the
     * passed comparator.
     *
     * @param scoreComparator comparator to sort scores
     */
    public ScoreList(Comparator<Double> scoreComparator) {
        this(scoreComparator, new ArrayList<>());
    }

    /**
     * Creates a new instance with the existing scores, sorting them and any future scores after the passed
     * comparator
     *
     * @param scoreComparator comparator to sort scores
     * @param scores initial scores
     */
    public ScoreList(Comparator<Double> scoreComparator, List<Double> scores) {
        addAll(scores);
        this.scoreComparator = scoreComparator;
        sortInternalList();
    }

    private void sortInternalList() {
        sort(scoreComparator);
    }

    /**
     * Adds a score to the list.
     *
     * @param score Score to be added
     */
    public void addScore(double score) {
        add(score);
    }

    /**
     * Gets the highest score currently added.
     *
     * @return highest score or null if no scores are recorded
     */
    public Double getHighScore() {
        if (isEmpty()) {
            return null;
        }
        return get(0);
    }

    /**
     * Returns whether the specified score is higher than or equal to the highest one recorded so far.
     *
     * @param other score to compare against
     * @return whether the score is the highest so far (or higher)
     */
    public boolean isHighScore(Double other) {
        return isEmpty() || scoreComparator.compare(getHighScore(), other) >= 0;
    }

    @Override
    public boolean add(Double aDouble) {
        boolean success = super.add(aDouble);
        sortInternalList();
        return success;
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        boolean success = super.addAll(c);
        sortInternalList();
        return success;
    }

}
