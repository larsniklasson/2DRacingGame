package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

/**
 * Represents a Map used in a race. Contains the different models which is connected to it, such as checkpoints,
 * spawn points etc.
 *
 * TODO way to nested with Shape objects, they should ideally be created in the world. Most models are related to them, making it hard to overcome smoothly.
 *
 * @author Daniel Sunnerberg
 */
public enum GameMap implements Disposable {

    MAP_1("maps/map1.tmx", "maps/map1.png"),
    MAP_2("maps/map2.tmx", "maps/map2.png");

    private final String mapPath;
    private final String overviewImagePath;
    private TiledMap tiledMap;

    private MapObjects mapObjects;

    /**
     * Creates a new GameMap.
     *
     * @param mapPath path to map source file
     * @param overviewImagePath path to map overview screenshot
     */
    GameMap(String mapPath, String overviewImagePath) {
        this.mapPath = mapPath;
        this.overviewImagePath = overviewImagePath;
    }

    /**
     * @return path to map source file
     */
    public String getPath() {
        return mapPath;
    }

    /**
     * @return path to map overview screenshot
     */
    public String getOverviewImagePath() {
        return overviewImagePath;
    }

    /**
     * @return the map converted to tiled format, this is done by #load(...)-method
     * @see #load(MapLoader)
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * Loads the map's objects using the specified MapLoader.
     * @param mapLoader loader to be used
     */
    public void load(MapLoader mapLoader) {
        tiledMap = mapLoader.loadMap(mapPath);
        mapObjects = mapLoader.getMapObjects();

        if (mapObjects.getSpawnPoints().isEmpty()) {
            throw new IllegalStateException("Found no spawn-areas on the map.");
        }
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }

    /**
     * @return all map objects in the mapZ
     */
    public MapObjects getMapObjects() {
        return mapObjects;
    }
}
