package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lars Niklasson on 2015-04-24.
 */
public class MathHelper {
    public static void scaleRect(Rectangle r, float scale){
        r.setX(r.getX()*scale);
        r.setY(r.getY() * scale);

        r.setWidth(r.getWidth() * scale);
        r.setHeight(r.getHeight() * scale);
    }

}
