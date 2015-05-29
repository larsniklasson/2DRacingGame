package edu.chl._2DRacingGame.controllers;

/**
 * @author Anton Ingvarsson
 */
public interface GameScreenListener extends MainMenuDisplayer{
    void resume();
    void pause();
    boolean isPauseable();


}
