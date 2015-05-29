package edu.chl._2DRacingGame.vehicle;

/**
 * Enum for difficulty-setting of AI.
 *
 *@author Lars Niklasson
 */
public enum Difficulty {
    Easy, Medium, Hard;

    /**
     *
     * @return A random Difficulty chosen from the values available
     */
    public static Difficulty getRandomDifficulty(){
        return values()[(int) (Math.random() * values().length)];
    }
}
