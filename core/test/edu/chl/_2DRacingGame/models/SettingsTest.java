package edu.chl._2DRacingGame.models;

import edu.chl._2DRacingGame.persistance.Persistor;
import org.junit.Test;
import edu.chl._2DRacingGame.persistance.DummyPersistor;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Daniel Sunnerberg
 */
public class SettingsTest {

    private final Persistor<Map<String, String>> persistor = new DummyPersistor<>();

    @Test
    public void testSave1() throws Exception {
        Settings settings = new Settings(persistor);
        assertTrue(settings.isEmpty());

        settings.put("foo", "bar");
        settings.save();

        Settings savedSettings = new Settings(persistor);
        savedSettings.load();
        assertEquals("bar", savedSettings.getSetting("foo"));
    }

    @Test
    public void testSave2() throws Exception {
        Settings s1 = new Settings(persistor, "s1");
        s1.put("foo", "bar");
        s1.save();

        Settings s2 = new Settings(persistor, "s2");
        s2.put("foo", "baz");
        s2.save();

        Settings savedS1 = new Settings(persistor, "s1");
        savedS1.load();
        assertEquals("bar", savedS1.getSetting("foo"));

        Settings savedS2 = new Settings(persistor, "s2");
        savedS2.load();
        assertEquals("baz", savedS2.getSetting("foo"));
    }

    @Test
    public void testLoad() throws Exception {
        Settings settings = new Settings(persistor);
        settings.load();
        assertNull(settings.getSetting("foo"));
    }

    @Test
    public void testAddSetting() throws Exception {
        Settings settings = new Settings(persistor);
        assertNull(settings.getSetting("foo"));

        settings.addSetting("foo", "bar");
        assertEquals("bar", settings.getSetting("foo"));
    }

}