package edu.chl._2DRacingGame.steering;

/**
 * Created by Lars Niklasson on 2015-05-17.
 */
public enum Difficulty {
    Easy, Medium, Hard;

    public static Difficulty getRandomDifficulty(){
        return values()[(int) (Math.random() * values().length)];
    }
}
