package edu.chl._2DRacingGame.persistence;

import java.lang.reflect.Type;

/**
 * Interface for classes with the ability to persist instances of classes to e.g disk by serialization.
 *
 * @author Daniel Sunnerberg
 */
public interface Persistor<T> {

    /**
     * Persist the passed instance to allow it to be retrieved at a later time.
     *
     * @param instance instance to be persisted
     * @param persistKey key which represents the instance to be persisted
     */
    void persist(T instance, String persistKey);

    /**
     * Retrieves a previously persisted instance.
     *
     * @param persistKey key of the instance to be retreieved
     * @param originType original type of the instance, needed because of Java generics limitations
     * @return instance previously persisted
     * @throws PersistorException thrown when no instance is found under specified key
     */
    T getPersistedInstance(String persistKey, Type originType) throws PersistorException;
}
