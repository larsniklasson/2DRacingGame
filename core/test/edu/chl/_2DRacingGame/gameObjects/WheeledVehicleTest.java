package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.steering.AISpeedHolder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Lars Niklasson on 2015-05-22.
 */
public class WheeledVehicleTest {

    float epsilon = 0.0001f;

    @Test
    public void placeTest(){
        World world = new World(new Vector2(0,0), true);
        WheeledVehicle wv = new WheeledVehicle(world) {
            @Override
            public AISpeedHolder getEasySpeeds() {
                return null;
            }

            @Override
            public AISpeedHolder getMediumSpeeds() {
                return null;
            }

            @Override
            public AISpeedHolder getHardSpeeds() {
                return null;
            }
        };

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5, 5);

        float xOffset = 10;
        float yOffset = 10;
        wv.createBody(shape, 1);
        wv.attachWheel(new Wheel(world, 3, 3, 1), new Vector2(xOffset, yOffset), true);

        Wheel w = wv.getWheels().get(0);


        float x = 100;
        float y = 100;
        Vector2 position = new Vector2(x,y);

        wv.place(new Vector2(x, y), 0);

        assertEquals(wv.getPosition().x, x, epsilon);
        assertEquals(wv.getPosition().y, y, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().x, x + xOffset, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y, y + yOffset, epsilon);


        wv.place(position, (float) (Math.PI / 2));
        assertEquals(w.getBody().getTransform().getPosition().x, x - xOffset, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y, y + yOffset, epsilon);

        wv.place(position, (float) (-Math.PI / 2));
        assertEquals(w.getBody().getTransform().getPosition().x, x + xOffset, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y, y - yOffset, epsilon);

        wv.place(position, (float) (Math.PI));
        assertEquals(w.getBody().getTransform().getPosition().x, x - xOffset, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y, y - yOffset, epsilon);


        wv.place(position, (float) (Math.PI / 4));
        assertEquals(w.getBody().getTransform().getPosition().x, x, epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y, y + Math.sqrt(xOffset * xOffset + yOffset * yOffset), epsilon);

        wv.place(position, (float) (-Math.PI / 4));
        assertEquals(w.getBody().getTransform().getPosition().x, x + Math.sqrt(xOffset * xOffset + yOffset * yOffset), epsilon);
        assertEquals(w.getBody().getTransform().getPosition().y , y, epsilon);







    }
}
