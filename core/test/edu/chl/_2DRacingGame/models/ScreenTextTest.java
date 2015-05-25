package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by daniel on 2015-05-25.
 */
public class ScreenTextTest {

    @Test
    public void testGetText() throws Exception {
        ScreenText text = new ScreenText(null);
        assertTrue(text.getText().isEmpty());

        text.setText("foo");
        assertEquals("foo", text.getText());

        text = new ScreenText("bar", null);
        assertEquals("bar", text.getText());
    }

    @Test
    public void testGetCoordinates() throws Exception {
        Vector2 position = new Vector2(5, 5);
        ScreenText text = new ScreenText(position);
        assertTrue(position.x == text.getX());
        assertTrue(position.y == text.getY());
    }


    @Test
    public void testSyncer() throws Exception {
        ScreenText text = new ScreenText("foo", null);
        text.setSyncer(() -> "bar");
        text.syncText();
        assertEquals("bar", text.getText());
    }
    
}