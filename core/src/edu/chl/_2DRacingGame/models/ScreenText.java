package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;


/**
 * @author Daniel Sunnerberg
 */
public class ScreenText {

    private String text;
    private final Vector2 position;
    private Color color = new Color(255, 255, 255, 1);

    public ScreenText(Vector2 position) {
        this("", position);
    }

    public ScreenText(String text, Vector2 position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
