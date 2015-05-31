package edu.chl._2DRacingGame.persistence;

/**
 * A class, or part of its data, which is persistable.
 *
 * @author Daniel Sunnerberg
 */
public interface Persistable {

    /**
     * Persists the instance or data selected by class.
     */
    void save();

    /**
     * Loads previously persisted data, allowing the class to use it.
     */
    void load();

}
