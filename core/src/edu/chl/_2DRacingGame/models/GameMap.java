package edu.chl._2DRacingGame.models;

/**
 * @author Daniel Sunnerberg
 */
public enum GameMap {

    MAP_1("map3.tmx"),
    MAP_2("map4.tmx");

    private final String mapPath;

    GameMap(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getPath() {
        return mapPath;
    }
}
