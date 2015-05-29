package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * A class capable of loading a map and its objects.
 *
 * @author Daniel Sunnerberg
 */
public interface MapLoader {

    /**
     * Loads the map from the specified path.
     *
     * @param mapPath path to load map from
     * @return the paths content, converted to a tiled map
     */
    TiledMap loadMap(String mapPath);

    /**
     * @return all map objects found in loaded map
     */
    MapObjects getMapObjects();

}
