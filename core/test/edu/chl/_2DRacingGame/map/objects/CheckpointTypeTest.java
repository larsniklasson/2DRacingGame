package edu.chl._2DRacingGame.map.objects;

import edu.chl._2DRacingGame.map.CheckpointType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointTypeTest {

    @Test
    public void testGetTypeFromName1() throws Exception {
        assertEquals(CheckpointType.INVISIBLE, CheckpointType.getTypeFromName(null));
        assertEquals(CheckpointType.LAP_END, CheckpointType.getTypeFromName("lap_end"));
        assertEquals(CheckpointType.LAP_START, CheckpointType.getTypeFromName("lAp_stART"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeFromName2() throws Exception {
        CheckpointType.getTypeFromName("non-existing-type-name");
    }

}