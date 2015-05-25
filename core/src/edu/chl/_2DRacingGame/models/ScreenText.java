package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;


/**
 * Represents a text which is expected to be drawn on a specific location.
 *
 * @author Daniel Sunnerberg
 */
public class ScreenText {

    private String text;
    private final Vector2 position;
    private Color color = new Color(255, 255, 255, 1);

    private ScreenTextSyncer syncer;

    /**
     * Creates a new instance with an empty string as text on the given location.
     *
     * @param position the position of the text
     */
    public ScreenText(Vector2 position) {
        this("", position);
    }

    /**
     * Creates a new instance with the specified text on the given location.
     *
     * @param text text to be drawn
     * @param position the position of the text
     */
    public ScreenText(String text, Vector2 position) {
        this.text = text;
        this.position = position;
    }

    /**
     * @return its current text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the text's x-coordinate
     */
    public float getX() {
        return position.x;
    }

    /**
     * @return the text's y-coordinate
     */
    public float getY() {
        return position.y;
    }

    /**
     * @return the texts color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @param syncer the one responsible for producing a synced text when needed
     */
    public void setSyncer(ScreenTextSyncer syncer) {
        this.syncer = syncer;
    }

    /**
     * Requests and sets the synced text from the ScreenTextSyncer if one is set.
     * Likely called before drawing the text on screen to allow the text to be changed as needed.
     */
    public void syncText() {
        if (syncer != null) {
            setText(syncer.getSyncedText());
        }
    }

}
