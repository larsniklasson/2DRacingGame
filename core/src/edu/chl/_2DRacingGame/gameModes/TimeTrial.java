package edu.chl._2DRacingGame.gameModes;

import edu.chl._2DRacingGame.world.GameWorld;

/**
 * @author  Daniel Sunnerberg
 */
public class TimeTrial implements GameMode {

    private final GameWorld gameWorld;

    public TimeTrial(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void lap() {
        System.out.println("LAP!");
    }
}
