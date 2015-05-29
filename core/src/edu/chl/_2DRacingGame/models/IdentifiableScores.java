package edu.chl._2DRacingGame.models;

import com.google.gson.reflect.TypeToken;
import edu.chl._2DRacingGame.persistance.Persistable;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.persistance.PersistorException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Helps identify previously persisted scores, using a set of identifiers.
 * E.g in a race, the scores should be unique to a specific map/mode.
 * Identifiers are compared using their toString method.
 *
 * @author Daniel Sunnerberg
 */
public class IdentifiableScores implements Persistable {

    private final Set<Object> identifiers;
    private final Comparator<Double> scoreComparator;
    private final Persistor<List<Double>> persistor;
    private ScoreList scores;

    /**
     * Creates a new IdentifiableScores instance with the specified identifiers.
     *
     * @param identifiers identifiers which identify previously persisted scores
     * @param scoreComparator comparator which sorts the scores
     * @param persistor persistor which will somehow store the scores to make them available later if needed
     */
    public IdentifiableScores(Set<Object> identifiers, Comparator<Double> scoreComparator, Persistor<List<Double>> persistor) {
        this.identifiers = identifiers;
        this.scoreComparator = scoreComparator;
        this.persistor = persistor;
    }

    /**
     * Finds previously persisted scores with the same identifiers. If none are found, an empty list will be
     * retrieved instead.
     *
     * To retrieve the found scores:
     * @see IdentifiableScores#getScores()
     */
    @Override
    public void load() {
        List<Double> scores;
        try {
            Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
            String persistanceKey = getPersistanceKey();
            scores = persistor.getPersistedInstance(persistanceKey, listType);
        } catch (PersistorException e) {
            scores = new ArrayList<>();
        }

        this.scores = new ScoreList(scoreComparator, scores);
    }

    private String getPersistanceKey() {
        StringBuilder key = new StringBuilder();
        for (Object identifier : identifiers) {
            key.append(identifier.toString());
        }
        key.append(".scores");
        return key.toString();
    }

    /**
     * Saves the scores, making them later identifiable and retrievable by same identifiers.
     * Any existing saved scores using same identifiers will be replaced.
     *
     * @see IdentifiableScores#load()
     */
    @Override
    public void save() {
        if (scores == null) {
            throw new IllegalStateException("No scores loaded. Try to find scores before saving them.");
        }

        String instanceFileName = getPersistanceKey();
        persistor.persist(scores, instanceFileName);
    }

    /**
     * Returns the scores found by #load().
     *
     * @return scores previously found by #load()
     * @see IdentifiableScores#load()
     */
    public ScoreList getScores() {
        if (scores == null) {
            throw new IllegalStateException("No scores has been set. You need to find one using #load.");
        }

        return scores;
    }
}
