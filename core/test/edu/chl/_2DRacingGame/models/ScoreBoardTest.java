package edu.chl._2DRacingGame.models;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class ScoreBoardTest {

    private ScoreBoard scoreBoard;
    private Player p1;
    private Player p2;

    @Before
    public void setUp() throws Exception {
        scoreBoard = new ScoreBoard();
        p1 = new Player("foo", null);
        p2 = new Player("bar", null);
    }

    @Test
    public void testAddResult() throws Exception {
        scoreBoard.addResult(p1, 4d);
        assertEquals(1, scoreBoard.getResults().size());
        scoreBoard.addResult(p2, 2d);
        assertEquals(2, scoreBoard.getResults().size());
    }

    @Test
    public void testAddResult2() throws Exception {
        scoreBoard.trackPlayers(Lists.newArrayList(p1, p2));
        assertEquals(2, scoreBoard.getResults().size());
        scoreBoard.addResult(p1, 50d);
        assertEquals(2, scoreBoard.getResults().size());
        scoreBoard.addResult(p2, 55d);
        assertEquals(2, scoreBoard.getResults().size());
    }

    @Test
    public void testGetResults() throws Exception {
        Player p1 = new Player("a", null);
        Player p2 = new Player("b", null);

        scoreBoard.addResult(p1, 4d);
        double winningTime = 2d;
        scoreBoard.addResult(p2, winningTime);

        RaceResult winner = scoreBoard.getResults().iterator().next();
        assertEquals(p2, winner.getPlayer());
        assertTrue(winningTime == winner.getTime());
    }

    @Test
    public void testIsWinner() throws Exception {
        Player p3 = new Player("c", null);

        assertFalse("If no players have finished the race, there are no winners", scoreBoard.isWinner(p1));
        assertNull(scoreBoard.getWinner());

        scoreBoard.addResult(p1, 4d);
        assertEquals(p1, scoreBoard.getWinner());
        assertTrue(scoreBoard.isWinner(p1));

        scoreBoard.addResult(p2, 2d);
        assertEquals(p2, scoreBoard.getWinner());
        assertTrue(scoreBoard.isWinner(p2));

        scoreBoard.addResult(p3, 8d);
        assertEquals(p2, scoreBoard.getWinner());
        assertTrue(scoreBoard.isWinner(p2));
    }

    @Test
    public void testTrackPlayers() throws Exception {
        scoreBoard.addResult(p1, 5d);
        scoreBoard.addResult(p2, 5d);

        List<Player> trackQueue = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            trackQueue.add(new Player("track" + i, null));
        }
        scoreBoard.trackPlayers(trackQueue);

        assertEquals(2 + trackQueue.size(), scoreBoard.getResults().size());

        for (RaceResult result : scoreBoard.getResults()) {
            if (result.getPlayer().getUserName().contains("track")) {
                assertEquals(null, result.getTime());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPosition() throws Exception {
        scoreBoard.addResult(p1, 5d);
        scoreBoard.getPosition(p2);
    }

    @Test
    public void testGetPosition2() throws Exception {
        scoreBoard.addResult(p1, 5d);
        assertEquals(1, scoreBoard.getPosition(p1));

        scoreBoard.addResult(p2, 2d);
        assertEquals(1, scoreBoard.getPosition(p2));
        assertEquals(2, scoreBoard.getPosition(p1));
    }

    @Test
    public void testGetPosition3() throws Exception {
        scoreBoard.trackPlayers(Lists.newArrayList(p1, p2));
        scoreBoard.addResult(p2, 5d);
        assertEquals(1, scoreBoard.getPosition(p2));

        scoreBoard.addResult(p1, 1d);
        assertEquals(1, scoreBoard.getPosition(p1));
        assertEquals(2, scoreBoard.getPosition(p2));
    }
}