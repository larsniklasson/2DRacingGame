package edu.chl._2DRacingGame.persistence;

/**
 * Exception which will be thrown when a persistence operation failed.
 *
 * @author Daniel Sunnerberg
 */
public class PersistorException extends Exception {
    public PersistorException(String message) {
        super(message);
    }
}
