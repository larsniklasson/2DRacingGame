package edu.chl._2DRacingGame.models;

import edu.chl._2DRacingGame.gameModes.GameMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Sunnerberg
 */
public class MapScoresTest {

    private ScoreList mapScores;

    @Before
    public void setUp() throws Exception {
        GameMode mode = mock(GameMode.class);
        // Use a typical "lower is better" comparator
        when(mode.getScoreComparator()).thenReturn(Double::compareTo);

        mapScores = new ScoreList(mode.getScoreComparator());
    }

    @Test
    public void testAddScore() throws Exception {
        assertTrue(mapScores.getScores().isEmpty());

        mapScores.addScore(4d);
        mapScores.addScore(8d);
        assertEquals(2, mapScores.getScores().size());

        List<Double> scores = mapScores.getScores();
        assertTrue(4d == scores.get(0));
        assertTrue(8d == scores.get(1));
    }

    @Test
    public void testGetHighScore() throws Exception {
        assertEquals(null, mapScores.getHighScore());

        mapScores.addScore(4d);
        assertTrue(4d == mapScores.getHighScore());

        mapScores.addScore(10d);
        assertTrue(4d == mapScores.getHighScore());

        mapScores.addScore(1d);
        assertTrue(1d == mapScores.getHighScore());
    }

    @Test
    public void testIsHighScore() throws Exception {
        assertTrue(mapScores.isHighScore(0d));

        mapScores.addScore(5d);
        assertTrue(mapScores.isHighScore(5d));

        mapScores.addScore(8d);
        assertFalse(mapScores.isHighScore(8d));

        mapScores.addScore(2d);
        assertTrue(mapScores.isHighScore(2d));
    }

}