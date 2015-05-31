package edu.chl._2DRacingGame.map.objects;

import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.map.objects.CheckpointDirection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointDirectionTest {

    private final Vector2 startPosition = new Vector2(10, 10);

    @Test
    public void testWest() throws Exception {
        Vector2[] invalidDirections = new Vector2[] {
                new Vector2(10, 10),
                new Vector2(10, 20),
                new Vector2(10, -20)
        };

        Vector2[] validDirections = new Vector2[] {
                new Vector2(11, 10),
                new Vector2(20, 20)
        };

        assertDirections(CheckpointDirection.WEST, invalidDirections, validDirections);
    }

    @Test
    public void testNorth() throws Exception {
        Vector2[] invalidDirections = new Vector2[] {
                new Vector2(10, 10),
                new Vector2(20, 20),
                new Vector2(10, 20)
        };

        Vector2[] validDirections = new Vector2[] {
                new Vector2(10, 5),
                new Vector2(5, 5)
        };

        assertDirections(CheckpointDirection.NORTH, invalidDirections, validDirections);
    }

    @Test
    public void testEast() throws Exception {
        Vector2[] invalidDirections = new Vector2[] {
                new Vector2(10, 10),
                new Vector2(20, 20),
                new Vector2(10, 20)
        };

        Vector2[] validDirections = new Vector2[] {
                new Vector2(9, 10),
                new Vector2(5, 5)
        };

        assertDirections(CheckpointDirection.EAST, invalidDirections, validDirections);
    }

    @Test
    public void testSouth() throws Exception {
        Vector2[] invalidDirections = new Vector2[] {
                new Vector2(10, 10),
                new Vector2(20, 10),
                new Vector2(5, 5)
        };

        Vector2[] validDirections = new Vector2[] {
                new Vector2(10, 11),
                new Vector2(0, 20)
        };

        assertDirections(CheckpointDirection.SOUTH, invalidDirections, validDirections);
    }

    private void assertDirections(CheckpointDirection test, Vector2[] invalidDirections, Vector2[] validDirections) {

        for (Vector2 invalidDirection : invalidDirections) {
            assertFalse(
                    String.format("[%s] Invalid entry expected for %s", test.name(), invalidDirection),
                    test.isValidEntry(startPosition, invalidDirection)
            );
        }

        for (Vector2 validDirection : validDirections) {
            assertTrue(
                    String.format("[%s] Valid entry expected for %s", test.name(), validDirection),
                    test.isValidEntry(startPosition, validDirection)
            );
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDirectionsFromNames1() {
        CheckpointDirection.getDirectionsFromNames("");
    }

    @Test
    public void testGetDirectionsFromNames2() {
        List<CheckpointDirection> directions = new ArrayList<>();
        directions.add(CheckpointDirection.NORTH);

        assertEquals(directions, CheckpointDirection.getDirectionsFromNames("north"));
        assertEquals(directions, CheckpointDirection.getDirectionsFromNames("NoRTH"));

        directions.add(CheckpointDirection.EAST);
        assertEquals(directions, CheckpointDirection.getDirectionsFromNames("north|east"));

        directions.add(CheckpointDirection.SOUTH);
        directions.add(CheckpointDirection.WEST);

        assertEquals(directions, CheckpointDirection.getDirectionsFromNames("north|east|south|west"));
    }

}