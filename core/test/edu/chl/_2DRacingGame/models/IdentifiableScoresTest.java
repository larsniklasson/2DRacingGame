package edu.chl._2DRacingGame.models;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.chl._2DRacingGame.persistance.Persistor;
import org.junit.Before;
import org.junit.Test;
import edu.chl._2DRacingGame.persistance.DummyPersistor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class IdentifiableScoresTest {

    private IdentifiableScores scores;
    private Set<Object> identifiers;
    private Persistor<List<Double>> listPersistor;
    private Comparator<Double> scoreComparator;

    @Before
    public void setUp() throws Exception {
        Object id1 = new Object() {
            @Override
            public String toString() {
                return "id1";
            }
        };
        Object id2 = new Object() {
            @Override
            public String toString() {
                return "id2";
            }
        };

        identifiers = Sets.newHashSet(id1, id2);
        scoreComparator = Double::compareTo;
        listPersistor = new DummyPersistor<>();
        scores = new IdentifiableScores(identifiers, scoreComparator, listPersistor);
    }

    @Test
    public void testFindSavedScores() throws Exception {
        scores.load();
        assertTrue("No previous save should yield empty scores", scores.getScores().isEmpty());

        scores.getScores().addAll(Lists.newArrayList(5d, 42d, 1d, 9d));
        ScoreList sortedValues = scores.getScores(); // Scores should be sorted after adding them
        scores.save();

        IdentifiableScores persistedScores = new IdentifiableScores(identifiers, scoreComparator, listPersistor);
        persistedScores.load();
        assertEquals("Should be able to find previous sorted scores", sortedValues, persistedScores.getScores());

        scores.getScores().add(0.1d);
        sortedValues = scores.getScores();
        scores.save();

        persistedScores = new IdentifiableScores(identifiers, scoreComparator, listPersistor);
        persistedScores.load();
        assertEquals("Should be able to save multiple times", sortedValues, persistedScores.getScores());
        assertEquals("Highscore shouldn't be altered by save", 0.1d, persistedScores.getScores().getHighScore(), 0);
    }


    @Test(expected = IllegalStateException.class)
    public void testInvalidGetScores() throws Exception {
        scores.getScores();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidSave() throws Exception {
        scores.save();
    }
}