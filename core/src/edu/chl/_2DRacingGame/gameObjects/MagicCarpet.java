package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.Assets;
import edu.chl._2DRacingGame.helperClasses.InputManager;
import edu.chl._2DRacingGame.world.GameWorld;

import java.util.Set;

/**
 * Created by Lars Niklasson on 2015-05-09.
 */
public class MagicCarpet extends Vehicle{
    private Tire tire;
    private static final float WIDTH = 20f/ GameWorld.PIXELS_PER_METER;
    private static final float HEIGHT = 30f/GameWorld.PIXELS_PER_METER;



    public MagicCarpet(World world) {
        super(world);

        Texture texture = Assets.magicCarpetBody;
        setSprite(new Sprite(texture));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);

        createBody(shape, 0.0001f);

        tire = new FlyingTire(world, WIDTH, HEIGHT, 0.5f);
        tire.setCharacteristics(10f, 0.2f, 15f, -5f, -0.2f);

        attachTire(tire, new Vector2(0,0), false);
    }

    @Override
    public void turnWheels(Set<InputManager.PressedKey> keys){
        float torque = 0;
        if(keys.contains(InputManager.PressedKey.Left)){
            torque = 3;
        }

        if (keys.contains(InputManager.PressedKey.Right)){
            torque = -3;
        }

        tire.getBody().applyAngularImpulse(0.1f * tire.getBody().getInertia() * -tire.getBody().getAngularVelocity(), true);
        tire.getBody().applyTorque(torque, true);
    }
}
