package edu.chl._2DRacingGame.models;

/**
 * @author Daniel Sunnerberg
 */
public enum GameMap {

    MAP_1("maps/map1.tmx", "maps/map1.png"),
    MAP_2("maps/map2.tmx", "maps/map2.png");

    private final String mapPath;
    private final String overviewImagePath;

    GameMap(String mapPath, String overviewImagePath) {
        this.mapPath = mapPath;
        this.overviewImagePath = overviewImagePath;
    }

    public String getPath() {
        return mapPath;
    }

    public String getOverviewImagePath() {
        return overviewImagePath;
    }
}
