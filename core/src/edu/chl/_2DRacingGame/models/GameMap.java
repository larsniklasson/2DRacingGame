package edu.chl._2DRacingGame.models;

/**
 * @author Daniel Sunnerberg
 */
public enum GameMap {

    PLACEHOLDER_MAP("map4.tmx");

    private final String mapPath;

    GameMap(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getPath() {
        return mapPath;
    }
}
