package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.math.Rectangle;

/**
 * Small helper-class.
 * @author Lars Niklasson
 */
class MathHelper {

    /**
     * Scales a rectangle.
     * @param r The rectangle to be scaled.
     * @param scale The scale
     */
    public static void scaleRect(Rectangle r, float scale){
        r.setX(r.getX()*scale);
        r.setY(r.getY() * scale);

        r.setWidth(r.getWidth() * scale);
        r.setHeight(r.getHeight() * scale);
    }

}
