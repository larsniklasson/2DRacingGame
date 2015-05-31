package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.map.Checkpoint;
import edu.chl._2DRacingGame.map.CheckpointDirection;
import edu.chl._2DRacingGame.map.CheckpointType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Daniel Sunnerberg
 */
public class CheckpointTest {

    @Test
    public void testIsValidEntry() throws Exception {
        final Vector2 begin = new Vector2(0, 0);
        Vector2 end = new Vector2(10, 10);

        Checkpoint checkpoint = new Checkpoint();
        assertFalse("Without any valid directions all should fail", checkpoint.isValidEntry(begin, end));
 
        checkpoint.addAllowedPassingDirection(CheckpointDirection.EAST);
        assertFalse("When only allowing EAST, passing from southwest should not be allowed", checkpoint.isValidEntry(begin, end));

        checkpoint = new Checkpoint();
        checkpoint.addAllowedPassingDirection(CheckpointDirection.WEST);
        assertTrue("Passing from southwest should be allowed if only allowing west", checkpoint.isValidEntry(begin, end));

        checkpoint.addAllowedPassingDirection(CheckpointDirection.SOUTH);
        assertTrue("Passing from southwest should be allowed if allowing southwest", checkpoint.isValidEntry(begin, end));
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(CheckpointType.LAP_END, new Checkpoint(CheckpointType.LAP_END).getType());
        assertEquals(CheckpointType.INVISIBLE, new Checkpoint().getType());
    }
}