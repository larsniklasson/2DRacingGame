package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;


/**
 * @author Daniel Sunnerberg
 */
public class ScreenText {

    private String text;
    private final Vector2 location;
    private Color color = new Color(255, 255, 255, 1);
    private Vector2 position;

    public ScreenText(Vector2 location) {
        this.location = location;
    }

    public ScreenText(String text, Vector2 location) {
        this.text = text;
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return location.x;
    }

    public float getY() {
        return location.y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
