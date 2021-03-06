package edu.chl._2DRacingGame.vehicle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Class used for linking sprites to Box2D-bodies.
 * @author Lars Niklasson
 */
public class SpriteUtils {
    public static void updateSprite(Body body, Sprite sprite, float scale){

        sprite.setPosition((body.getWorldCenter().x * scale) - sprite.getWidth() / 2,
                (body.getWorldCenter().y * scale) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getTransform().getRotation()));
    }

    public static void rotateSpriteTo(Sprite sprite, float radians){ //maybe not needed but was used for MP
        sprite.setRotation((float) Math.toDegrees(radians));
    }

    public static void rotateSpriteBy(Sprite sprite, float radians){
        sprite.setRotation((float) (sprite.getRotation() + Math.toDegrees(radians)));
    }


}
