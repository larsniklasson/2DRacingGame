package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.chl._2DRacingGame.gameModes.GameMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public class MapScores {

    private final GameMap map;
    private transient final GameMode mode;

    private final Comparator<Double> highscoreComparator;
    private final List<Double> scores;

    private MapScores(GameMap map, GameMode mode) {
        this(map, mode, new ArrayList<>());
    }

    private MapScores(GameMap map, GameMode mode, List<Double> scores) {
        this.map = map;
        this.mode = mode;
        this.scores = scores;
        highscoreComparator = mode.getHighscoreComparator();
    }

    public void addScore(double score) {
        scores.add(score);
        scores.sort(highscoreComparator);
    }

    public Double getHighScore() {
        if (scores.isEmpty()) {
            return null;
        }
        return scores.get(0);
    }

    public boolean isHighScore(Double other) {
        return scores.isEmpty() || highscoreComparator.compare(getHighScore(), other) > 0;
    }

    private static String getInstanceFileName(GameMap map, GameMode mode) {
        return map.toString() + "_" + mode.getClass().getSimpleName() + ".scores";
    }

    /**
     * Saves the instance to disk, replacing any existing scores for the matching
     * map and mode.
     */
    public void persist() {
        Gdx.app.log("MapScores", "Saving scores to disk.");
        // At retrieve time we know everything but the actual scores
        String serialized = new Gson().toJson(scores);
        FileHandle fileHandle = Gdx.files.local(getInstanceFileName(map, mode));
        fileHandle.writeString(serialized, false);
    }

    public static MapScores getInstance(GameMap map, GameMode mode) {
        FileHandle serializedInstance = Gdx.files.local(getInstanceFileName(map, mode));
        try {
            String serialized = serializedInstance.readString();
            Gdx.app.log("MapScores", "Found stored scores for this game.");
            Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
            List<Double> scores = new Gson().fromJson(serialized, listType);

            return new MapScores(map, mode, scores);

        } catch (GdxRuntimeException e) {
            Gdx.app.log("MapScores", "Found no stored scores. Creating now.");
        }

        return new MapScores(map, mode);
    }

}
