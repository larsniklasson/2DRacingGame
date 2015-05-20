package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.Gdx;
import com.google.gson.reflect.TypeToken;
import edu.chl._2DRacingGame.gameModes.GameMode;
import edu.chl._2DRacingGame.persistance.Persistor;
import edu.chl._2DRacingGame.persistance.PersistorException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Acts as a wrapper around ScoreList, keeping track of the player's scores on the specified map/mode-combination.
 *
 * @author Daniel Sunnerberg
 */
public class MapScores {

    private final GameMap map;
    private final GameMode mode;

    private final Persistor<List<Double>> persistor;
    private ScoreList scores;

    /**
     * Creates a new MapScores instance which helps track player's scores on the specified map/mode-combination.
     *
     * @param map map to track scores for
     * @param mode mode to track scores for
     * @param persistor persistor which will somehow store the scores to make them available later if needed
     */
    public MapScores(GameMap map, GameMode mode, Persistor<List<Double>> persistor) {
        this.map = map;
        this.mode = mode;
        this.persistor = persistor;
    }

    /**
     * Finds scores saved by previous game instances and loads these. If none are found, an empty list will be
     * retrieved instead.
     *
     * To retrieve the found scores:
     * @see MapScores#getScores()
     */
    public void findSavedScores() {
        List<Double> scores;
        try {
            Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
            String persistanceKey = getPersistanceKey(map, mode);
            scores = persistor.getPersistedInstance(persistanceKey, listType);
        } catch (PersistorException e) {
            scores = new ArrayList<>();
        }

        this.scores = new ScoreList(mode.getScoreComparator(), scores);
    }

    private String getPersistanceKey(GameMap map, GameMode mode) {
        return map.toString() + "_" + mode.getModeName() + ".scores";
    }

    /**
     * Saves the scores saved for the map/mode combination, making them available for later retrieval.
     * Any existing saved scores will be replaced.
     *
     * @see MapScores#findSavedScores()
     */
    public void save() {
        if (scores == null) {
            throw new IllegalStateException("No scores loaded. Try to find scores before saving them.");
        }

        String instanceFileName = getPersistanceKey(map, mode);
        persistor.persist(scores, instanceFileName);
    }

    /**
     * Returns the scores found by #findSavedScores().
     *
     * @return scores previously found by #findInstance()
     * @see MapScores#findSavedScores()
     */
    public ScoreList getScores() {
        if (scores == null) {
            throw new IllegalStateException("No scores has been set. You need to find one using #findSavedScores.");
        }

        return scores;
    }
}
