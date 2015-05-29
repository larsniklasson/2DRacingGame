package edu.chl._2DRacingGame.persistance;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple persistor imitator which stores the instances to a map to mimick persistance.
 * Used for testing purposes.
 *
 * @author Daniel Sunnerberg
 */
public class DummyPersistor<T> implements Persistor<T> {

    private final Map<String, T> storage = new HashMap<>();

    @Override
    public void persist(T instance, String persistKey) {
        storage.put(persistKey, instance);
    }

    @Override
    public T getPersistedInstance(String persistKey, Type originType) throws PersistorException {
        if (! storage.containsKey(persistKey)) {
            throw new PersistorException("No persisted instance found");
        }

        return storage.get(persistKey);
    }

}
