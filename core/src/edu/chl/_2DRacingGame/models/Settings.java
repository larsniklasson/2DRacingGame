package edu.chl._2DRacingGame.models;

import com.google.gson.reflect.TypeToken;
import edu.chl._2DRacingGame.persistance.Persistable;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.persistance.PersistorException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of settings using a simpole map-structure. All keys and values are to be supplied as strings.
 * Can be persisted, see methods #save(), #load(). When using persistance and have multiple instances for different
 * type of settings, make sure to use #Settings(persistor, settingsAlias) constructor to avoid having instances
 * overwriting each other.
 *
 * @author Daniel Sunnerberg
 * @see Settings#load()
 * @see Settings#save()
 */
public class Settings extends HashMap<String, String> implements Persistable {

    private static final String DEFAULT_PERSIST_KEY = "settings";

    private final Persistor<Map<String, String>> persistor;
    private final String persistKey;

    /**
     * Creates a new instance with no stored settings.
     * If persisted it'll be stored as the applications default settings.
     *
     * @param persistor persistor which will somehow store the settings to make them available later if needed
     */
    public Settings(Persistor<Map<String, String>> persistor) {
        this(persistor, DEFAULT_PERSIST_KEY);
    }

    /**
     * Creates a new instance with no stored settings.
     * If persisted, it'll be stored under the specified persistKey
     *
     * @param persistor persistor which will somehow store the settings to make them available later if needed
     * @param persistKey under which key the persistor will store the settings
     */
    public Settings(Persistor<Map<String, String>> persistor, String persistKey) {
        super();
        this.persistor = persistor;
        this.persistKey = persistKey;
    }

    /**
     * Saves the settings, making them available for later use.
     */
    @Override
    public void save() {
        persistor.persist(this, persistKey);
    }

    /**
     * Loads settings previously saved under the specified persistKey.
     */
    @Override
    public void load() {
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        try {
            Map<String, String> savedSettings = persistor.getPersistedInstance(persistKey, type);
            putAll(savedSettings);
        } catch (PersistorException e) {
            // If we can't load existing settings, assume none are set.
            return;
        }
    }

    /**
     * Adds a setting and its value.
     * NOTE: alias of #put(key, value)
     *
     * @param key key for the setting
     * @param value the settings value
     */
    public void addSetting(String key, String value) {
        put(key, value);
    }

    /**
     * Retreives the setting stored under the specified key.
     * NOTE: alias of #get(key)
     *
     * @param key
     */
    public String getSetting(String key) {
        return get(key);
    }

}
