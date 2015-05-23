package edu.chl._2DRacingGame.models;

import com.google.common.collect.Lists;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.persistance.Persistor;
import org.junit.Before;
import org.junit.Test;
import persistance.DummyPersistor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Sunnerberg
 */
public class MapScoresTest {

    private GameMap gameMap;
    private GameMode gameMode;
    private MapScores mapScores;
    private Persistor<List<Double>> listPersistor;

    @Before
    public void setUp() throws Exception {
        // Crazy. Assures that we always have one map defined, which is the only good thing about this line.
        gameMap = GameMap.values()[0];

        gameMode = mock(GameMode.class);
        when(gameMode.getModeName()).thenReturn("fooMode");

        listPersistor = new DummyPersistor<>();
        mapScores = new MapScores(gameMap, gameMode, listPersistor);
    }

    @Test
    public void testFindSavedScores() throws Exception {
        mapScores.load();
        assertTrue("No previous save should yield empty scores", mapScores.getScores().isEmpty());

        ArrayList<Double> values = Lists.newArrayList(5d, 42d, 1d, 9d);
        mapScores.getScores().addAll(values);
        List<Double> sortedValues = mapScores.getScores(); // Scores should be sorted after adding them
        mapScores.save();

        MapScores persistedScores = new MapScores(gameMap, gameMode, listPersistor);
        persistedScores.load();
        assertEquals("Should be able to find previous sorted scores", sortedValues, persistedScores.getScores());

        mapScores.getScores().add(0.1d);
        sortedValues = mapScores.getScores();
        mapScores.save();

        persistedScores = new MapScores(gameMap, gameMode, listPersistor);
        persistedScores.load();
        assertEquals("Should be able to save multiple times", sortedValues, persistedScores.getScores());
        assertEquals("Highscore shouldn't be altered by save", 0.1d, persistedScores.getScores().getHighScore(), 0);
    }


    @Test(expected = IllegalStateException.class)
    public void testInvalidGetScores() throws Exception {
        mapScores.getScores();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidSave() throws Exception {
        mapScores.save();
    }
}