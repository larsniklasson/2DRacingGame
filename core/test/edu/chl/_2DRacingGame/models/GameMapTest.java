package edu.chl._2DRacingGame.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class GameMapTest {

    @Test
    public void testGetPath() throws Exception {
        GameMap[] maps = GameMap.values();
        assertTrue(maps.length > 0 && maps[0].getPath() != null);
    }
}