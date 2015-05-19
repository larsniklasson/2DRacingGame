package edu.chl._2DRacingGame.persistance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists instances of classes to disk using Gson and LibGDX's file handler.
 *
 * @author Daniel Sunnerberg
 */
public class DiskPersistor<T> implements Persistor<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void persist(T instance, String persistKey) {
        String serializedInstance = new Gson().toJson(instance);
        FileHandle fileHandle = Gdx.files.local(persistKey);
        fileHandle.writeString(serializedInstance, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getPersistedInstance(String persistKey, Type originType) throws PersistorException {
        FileHandle persistedInstance = Gdx.files.local(persistKey);
        List<Double> scores = new ArrayList<>();
        try {
            String serialized = persistedInstance.readString();
            return new Gson().fromJson(serialized, originType);
        } catch (GdxRuntimeException e) {
            throw new PersistorException("Found no persisted instance at that key.");
        }
    }
}
