package edu.chl._2DRacingGame.models;

import com.google.common.collect.Lists;
import edu.chl._2DRacingGame.gameModes.GameMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Sunnerberg
 */
public class ScoreListTest {

    private ScoreList scoreList;

    @Before
    public void setUp() throws Exception {
        scoreList = new ScoreList(Double::compareTo);
    }

    @Test
    public void testConstructor() throws Exception {
        List<Double> scores = Lists.newArrayList(5d, 9d, 1d, 10d);
        scoreList = new ScoreList(Double::compareTo, scores);

        assertTrue(1d == scoreList.getHighScore());
        assertTrue(9d == scoreList.getScores().get(2));
    }

    @Test
    public void testAddScore() throws Exception {
        assertTrue(scoreList.getScores().isEmpty());

        scoreList.addScore(4d);
        scoreList.addScore(8d);
        assertEquals(2, scoreList.getScores().size());

        List<Double> scores = scoreList.getScores();
        assertTrue(4d == scores.get(0));
        assertTrue(8d == scores.get(1));
    }

    @Test
    public void testGetHighScore() throws Exception {
        assertEquals(null, scoreList.getHighScore());

        scoreList.addScore(4d);
        assertTrue(4d == scoreList.getHighScore());

        scoreList.addScore(10d);
        assertTrue(4d == scoreList.getHighScore());

        scoreList.addScore(1d);
        assertTrue(1d == scoreList.getHighScore());
    }

    @Test
    public void testIsHighScore() throws Exception {
        assertTrue(scoreList.isHighScore(0d));

        scoreList.addScore(5d);
        assertTrue(scoreList.isHighScore(5d));

        scoreList.addScore(8d);
        assertFalse(scoreList.isHighScore(8d));

        scoreList.addScore(2d);
        assertTrue(scoreList.isHighScore(2d));
    }

}