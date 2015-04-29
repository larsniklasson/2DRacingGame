package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Note: All code written by Lars Niklasson; just extracted it.
 *
 * @author Lars Niklasson
 */
public class ShapeFactory {

    public static Shape createShape(MapObject object, float scale) {

        if (object instanceof RectangleMapObject) {
            Rectangle r = ((RectangleMapObject) object).getRectangle();
            MathHelper.scaleRect(r, 1 / scale);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(r.getWidth() / 2, r.getHeight() / 2, r.getCenter(new Vector2()), 0);
            return shape;
        }

        if (object instanceof PolygonMapObject) {
            Polygon p = ((PolygonMapObject) object).getPolygon();
            float x = p.getX() / scale;
            float y = p.getY() / scale;

            float[] vertices = p.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] / scale;
                if (i % 2 == 0) {
                    vertices[i] += x;
                } else {
                    vertices[i] += y;
                }
            }

            PolygonShape shape = new PolygonShape();
            shape.set(vertices);
            return shape;
        }

        //only works for circles
        if (object instanceof EllipseMapObject) {
            Ellipse e = ((EllipseMapObject) object).getEllipse();

            CircleShape shape = new CircleShape();

            shape.setRadius(e.width / 2 / scale);
            shape.setPosition(new Vector2(e.x / scale + shape.getRadius(), e.y / scale + shape.getRadius()));
            return shape;
        }

        //only works for a straight line with only 2 vertices
        if (object instanceof PolylineMapObject) {
            Polyline pl = ((PolylineMapObject) object).getPolyline();

            float[] vertices = pl.getVertices();

            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] / scale;
            }

            float x = pl.getX() / scale;
            float y = pl.getY() / scale;

            for (int i = 0; i < vertices.length; i++) {
                if (i % 2 == 0) {
                    vertices[i] += x;
                } else {
                    vertices[i] += y;
                }
            }
            EdgeShape shape = new EdgeShape();
            shape.set(vertices[0], vertices[1], vertices[2], vertices[3]);

            return shape;

        }

        return null;
    }
}
