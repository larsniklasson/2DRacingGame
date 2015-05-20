package edu.chl._2DRacingGame.models;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class ScoreListTest {

    private ScoreList scores;

    @Before
    public void setUp() throws Exception {
        scores = new ScoreList(Double::compareTo);
    }

    @Test
    public void testConstructor() throws Exception {
        List<Double> scores = Lists.newArrayList(5d, 9d, 1d, 10d);
        this.scores = new ScoreList(Double::compareTo, scores);

        assertTrue(1d == this.scores.getHighScore());
        assertTrue(9d == this.scores.get(2));
    }

    @Test
    public void testAddScore() throws Exception {
        assertTrue(scores.isEmpty());

        scores.addScore(4d);
        scores.addScore(8d);
        assertEquals(2, scores.size());

        assertTrue(4d == scores.get(0));
        assertTrue(8d == scores.get(1));
    }

    @Test
    public void testGetHighScore() throws Exception {
        assertEquals(null, scores.getHighScore());

        scores.addScore(4d);
        assertTrue(4d == scores.getHighScore());

        scores.addScore(10d);
        assertTrue(4d == scores.getHighScore());

        scores.addScore(1d);
        assertTrue(1d == scores.getHighScore());
    }

    @Test
    public void testIsHighScore() throws Exception {
        assertTrue(scores.isHighScore(0d));

        scores.addScore(5d);
        assertTrue(scores.isHighScore(5d));

        scores.addScore(8d);
        assertFalse(scores.isHighScore(8d));

        scores.addScore(2d);
        assertTrue(scores.isHighScore(2d));
    }

}