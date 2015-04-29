package edu.chl._2DRacingGame.gameModes;

import edu.chl._2DRacingGame.models.ScreenText;

import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public abstract class GameMode implements LapListener {
    public List<ScreenText> getScreenTexts() {
        return null;
    }
}
