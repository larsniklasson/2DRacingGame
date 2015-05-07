package edu.chl._2DRacingGame.gameObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-07.
 */
public class MagicCarpet implements Vehicle{
    private Tire tire;
    private Sprite sprite;

    public MagicCarpet(World world, float width, float height) {

        tire = new Tire(world, width, height, 0.5f);
        tire.setCharacteristics(10f, 0.2f, 15f, -5f, -0.2f);
        tire.getBody().setUserData(this);

        Texture texture = new Texture(Gdx.files.internal("magiccarpet.png"));
        sprite = new Sprite(texture);




    }


    public void update(Set<InputManager.PressedKey> keys){
        tire.update(keys);

        float torque = 0;
        if(keys.contains(InputManager.PressedKey.Left)){
            torque = 3;
        }

        if (keys.contains(InputManager.PressedKey.Right)){
            torque = -3;
        }

        tire.getBody().applyAngularImpulse(0.1f * tire.getBody().getInertia() * -tire.getBody().getAngularVelocity(), true);
        tire.getBody().applyTorque(torque, true);

        updateSprite();

    }

    private void updateSprite() {
        sprite.setPosition((tire.getBody().getWorldCenter().x * GameWorld.PIXELS_PER_METER) - sprite.getWidth() / 2,
                (tire.getBody().getWorldCenter().y * GameWorld.PIXELS_PER_METER) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(tire.getBody().getAngle()));
    }

    @Override
    public List<Sprite> getSprites() {
        List<Sprite> list = new ArrayList<>();
        list.add(sprite);
        return list;
    }

    @Override
    public void moveTo(Vector2 position, float angle) {
        tire.getBody().setTransform(position, angle);
    }
}
