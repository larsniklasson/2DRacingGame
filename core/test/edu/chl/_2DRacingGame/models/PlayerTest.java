package edu.chl._2DRacingGame.models;

import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Sunnerberg
 */
public class PlayerTest {

    private Vehicle vehicle;

    @Before
    public void setUp() {
        vehicle = mock(Vehicle.class);
    }

    @Test
    public void testGetUserName() throws Exception {
        assertEquals("foo", new Player("foo", null).getUserName());
        assertFalse(new Player().getUserName().isEmpty());
    }

    @Test
    public void testGetVehicle() throws Exception {
        assertEquals(vehicle, new Player("foo", vehicle).getVehicle());

        Player player = new Player();
        player.setVehicle(vehicle);
        assertEquals(vehicle, player.getVehicle());
    }

    @Test
    public void testIsControlledByClient() throws Exception {
        assertFalse(new Player().isControlledByClient());

        Player player = new Player();
        player.setIsControlledByClient(true);
        assertTrue(player.isControlledByClient());
    }

    @Test
    public void testEquals() throws Exception {
        Vehicle vehicle = mock(Vehicle.class);

        Player p1 = new Player("foo", vehicle);
        assertEquals(p1, p1);

        Player p2 = new Player();
        assertNotEquals(p1, p2);

        p2.setVehicle(vehicle);
        assertNotEquals(p1, p2);
    }

    @Test
    public void testGetVehicleType() throws Exception {
        assertNull(new Player().getVehicleType());
    }
}