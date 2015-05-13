package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * @author Daniel Sunnerberg
 */
public enum GameMap {

    PLACEHOLDER_MAP("map3.tmx");

    private final String tiledMapPath;

    GameMap(String tiledMapPath) {
        this.tiledMapPath = tiledMapPath;
    }

    public TiledMap load() {
        // TODO Map loader should probably be injected
        return new TmxMapLoader().load(tiledMapPath);
    }
}
