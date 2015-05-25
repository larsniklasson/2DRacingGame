package edu.chl._2DRacingGame.models;

/**
 * Represents a class which is able to sync ScreenTexts.
 *
 * @author Daniel Sunnerberg
 */
@FunctionalInterface
public interface ScreenTextSyncer {

    /**
     * Gets a new syncted text for the ScreenText-instance.
     *
     * @return new synced text
     */
    String getSyncedText();

}
