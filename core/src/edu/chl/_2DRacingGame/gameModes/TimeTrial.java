package edu.chl._2DRacingGame.gameModes;

import com.badlogic.gdx.math.Vector2;
import edu.chl._2DRacingGame.models.ScreenText;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * @author  Daniel Sunnerberg
 */
public class TimeTrial extends GameMode {

    private int currentLap = 1;
    private final int lapGoal;

    private final ScreenText currentLapText;
    private final ScreenText currentRaceTimeText;

    public TimeTrial(int lapGoal, RaceListener listener) {
        super(listener);
        this.lapGoal = lapGoal;

        currentLapText = new ScreenText(new Vector2(1200, 670));
        addScreenText(currentLapText);
        currentRaceTimeText = new ScreenText(new Vector2(1200, 655));
        addScreenText(currentRaceTimeText);

        syncTexts();
    }

    @Override
    public void syncTexts() {
        currentLapText.setText(String.format("Lap %d/%d", currentLap, lapGoal));
        currentRaceTimeText.setText(getStopWatch().elapsed(TimeUnit.MILLISECONDS) / 1000d + "s");
    }

    @Override
    public void lap() {
        syncTexts();

        if (currentLap == lapGoal) {
            getStopWatch().stop();
            double elapsedTime = getStopWatch().elapsed(TimeUnit.MILLISECONDS) / 1000d;
            for (RaceListener listener : getListeners()) {
                listener.raceFinished(elapsedTime, "You drove the track in: " + elapsedTime + " seconds.");
            }
        }

        currentLap++;
    }

    @Override
    public Comparator<Double> getScoreComparator() {
        return (a, b) -> a.compareTo(b);
    }

    @Override
    public String getModeName() {
        return "TimeTrial";
    }

}
