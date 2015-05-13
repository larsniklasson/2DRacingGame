package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.chl._2DRacingGame.gameModes.GameMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public class MapScoresPersistor {

    private final GameMap map;
    private final GameMode mode;

    private MapScores instance;

    public MapScoresPersistor(GameMap map, GameMode mode) {
        this.map = map;
        this.mode = mode;
    }

    /**
     * Finds a MapScore instance with the unique map and mode.
     * If an instance with the same setup has been persisted before, the found instance will have the same scores.
     * Otherwise, a new instance will be created.
     *
     * @see MapScoresPersistor#getInstance()
     */
    public void findInstance() {
        FileHandle serializedInstance = Gdx.files.local(getInstanceFileName(map, mode));

        List<Double> scores = new ArrayList<>();
        try {
            String serialized = serializedInstance.readString();
            Gdx.app.log("MapScores", "Found stored scores for this game.");
            Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
            scores = new Gson().fromJson(serialized, listType);
        } catch (GdxRuntimeException e) {
            Gdx.app.log("MapScores", "Found no stored scores. Creating now.");
        }

        instance = new MapScores(map, mode, scores);
    }

    private String getInstanceFileName(GameMap map, GameMode mode) {
        return map.toString() + "_" + mode.getClass().getSimpleName() + ".scores";
    }

    /**
     * Saves the instance retrieved by findInstance to disk, making the scores available for the map/mode combination later.
     * Any existing persisted instance will be replaced.
     *
     * @see MapScoresPersistor#findInstance()
     */
    public void persistInstance() {
        if (instance == null) {
            throw new IllegalStateException("An instance must be found before any persisting can be performed.");
        }

        Gdx.app.log("MapScores", "Saving scores to disk.");
        // At retrieve time we know everything but the actual scores
        String serialized = new Gson().toJson(instance.getScores());
        FileHandle fileHandle = Gdx.files.local(getInstanceFileName(map, mode));
        fileHandle.writeString(serialized, false);
    }

    /**
     * Returns the instance found by #findInstance().
     *
     * @return instance previously found by #findInstance()
     * @see MapScoresPersistor#findInstance()
     */
    public MapScores getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No instance has been set. You need to find one using #findInstance.");
        }

        return instance;
    }
}
