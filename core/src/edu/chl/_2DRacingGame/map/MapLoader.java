package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * @author Daniel Sunnerberg
 */
public interface MapLoader {
    TiledMap loadMap(String mapPath);
    void insertMapObjects(GameMap gameMap);
}
