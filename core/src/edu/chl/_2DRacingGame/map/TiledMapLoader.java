package edu.chl._2DRacingGame.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import edu.chl._2DRacingGame.utils.ShapeFactory;
import edu.chl._2DRacingGame.map.objects.*;

import java.util.Iterator;
import java.util.List;

/**
 * A map loader which loads TiledMaps and related objects.
 *
 * NOTE: A lot of the logic is written by Lars, I've (Daniel) mostly extracted it.
 * @author Lars Niklasson
 * @author Daniel Sunnerberg
 */
public class TiledMapLoader implements MapLoader {

    private TiledMap tiledMap;
    private final float scaleFactor;

    private final MapObjects mapObjects = new MapObjects();

    /**
     * Creates a new MapLoader with the specified scale.
     *
     * @param scaleFactor scale to be used when loading shapes
     */
    public TiledMapLoader(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TiledMap loadMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        loadMapObjects();
        return tiledMap;
    }

    private void loadMapObjects() {

        MapLayers ml = tiledMap.getLayers();
        Iterator<MapLayer> it = ml.iterator();

        while (it.hasNext()) {
            MapLayer layer = it.next();
            Iterator<MapObject> it2 = layer.getObjects().iterator();

            while (it2.hasNext()) {
                MapObject object = it2.next();
                Shape shape = ShapeFactory.createShape(object, scaleFactor);
                String objectType = (String) object.getProperties().get("type");
                String objectName = object.getName();

                processMapObject(objectName, objectType, object, shape);
            }
        }
    }

    private void processMapObject(String objectName, String objectType, MapObject object, Shape shape) {

        switch (objectName) {
            case "dirt":
            case "ice":
            case "sand":
                insertTrackSection(objectName, shape);
                break;
            case "solid":
                insertImmovable(shape);
                break;
            case "path":
                insertPath(object);
                break;
            case "spawn_pos":
                insertSpawnPosition(object);
                break;
        }

        if (objectType != null && objectType.equals("checkpoint")) {
            insertCheckpoint(object, shape);
        }
    }

    private void insertTrackSection(String objectName, Shape shape) {
        GroundMaterial material = GroundMaterialFactory.create(objectName);
        mapObjects.addTrackSection(new TrackSection(shape, material));
    }

    private void insertImmovable(Shape shape) {
        mapObjects.addImmovable(new Immovable(shape));
    }

    private void insertPath(MapObject object) {
        if (!(object instanceof EllipseMapObject)) {
            throw new IllegalStateException("Path must be an Ellipse in Tiled");
        }

        Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
        Vector2 wayPoint = new Vector2(ellipse.x / scaleFactor, ellipse.y / scaleFactor);
        mapObjects.addWayPoint(wayPoint);
    }

    private void insertSpawnPosition(MapObject object) {
        if (!(object instanceof PolylineMapObject)) {
            throw new IllegalStateException("spawn_pos must be a PolyLine in Tiled");
        }

        Polyline pl = ((PolylineMapObject) object).getPolyline();
        float[] vertices = pl.getVertices();
        Vector2 v = new Vector2(vertices[2] - vertices[0], vertices[3] - vertices[1]);

        Vector2 position = new Vector2(vertices[0], vertices[1]);
        float angle = (float) (v.angleRad() - Math.PI / 2);

        mapObjects.addSpawnPoint(new SpawnPoint(position, angle));
    }

    private void insertCheckpoint(MapObject object, Shape shape) {
        CheckpointType type = CheckpointType.getTypeFromName(
                (String) object.getProperties().get("checkpointType")
        );

        List<CheckpointDirection> directions = CheckpointDirection.getDirectionsFromNames(
                (String) object.getProperties().get("checkpointDirection")
        );

        Checkpoint checkpoint = new Checkpoint(type);
        for (CheckpointDirection direction : directions) {
            checkpoint.addAllowedPassingDirection(direction);
        }

        mapObjects.addCheckpoint(checkpoint, shape);
    }

    @Override
    public MapObjects getMapObjects() {
        return mapObjects;
    }
    
}
