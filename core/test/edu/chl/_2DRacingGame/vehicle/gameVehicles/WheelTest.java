package edu.chl._2DRacingGame.vehicle.gameVehicles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.mapobjects.Dirt;
import edu.chl._2DRacingGame.mapobjects.GroundMaterial;
import edu.chl._2DRacingGame.mapobjects.Ice;
import static org.junit.Assert.*;

import edu.chl._2DRacingGame.vehicle.gameVehicles.Wheel;
import org.junit.Test;

/**
 * Created by Lars Niklasson on 2015-05-22.
 */
public class WheelTest {
    float epsilon = 0.0001f;

    @Test
    public void testAddingAndRemovingGroundMaterials(){
        float driveForce = 2;
        float maxLateralImpulse = 3;
        float maxForwardSpeed = 4;
        float maxBackwardSpeed = 5;
        float roadFriction = 6;

        GroundMaterial gm1 = new Ice();
        GroundMaterial gm2 = new Dirt();


        Wheel w = new Wheel(new World(new Vector2(0,0), true), 10,20,1);
        w.setCharacteristics(driveForce, maxLateralImpulse, maxForwardSpeed, maxBackwardSpeed, roadFriction);

        w.addGroundMaterial(gm1);

        w.updateValues();

        assertEquals(w.getCurrentMaxBackwardSpeed(), maxBackwardSpeed * gm1.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentMaxForwardSpeed(), maxForwardSpeed * gm1.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentBackwardsFriction(), roadFriction * gm1.getDrag(), epsilon);
        assertEquals(w.getCurrentMaxLateralImpulse(), maxLateralImpulse * gm1.getDrift(), epsilon);

        w.addGroundMaterial(gm2);
        w.updateValues();

        assertEquals(w.getCurrentMaxBackwardSpeed(), maxBackwardSpeed * gm2.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentMaxForwardSpeed(), maxForwardSpeed * gm2.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentBackwardsFriction(), roadFriction * gm2.getDrag(), epsilon);
        assertEquals(w.getCurrentMaxLateralImpulse(), maxLateralImpulse * gm2.getDrift(), epsilon);

        w.removeGroundMaterial(gm1);
        w.updateValues();
        assertEquals(w.getCurrentMaxBackwardSpeed(), maxBackwardSpeed * gm2.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentMaxForwardSpeed(), maxForwardSpeed * gm2.getSpeedFactor(), epsilon);
        assertEquals(w.getCurrentBackwardsFriction(), roadFriction * gm2.getDrag(), epsilon);
        assertEquals(w.getCurrentMaxLateralImpulse(), maxLateralImpulse * gm2.getDrift(), epsilon);

        w.removeGroundMaterial(gm2);
        w.updateValues();

        assertEquals(w.getCurrentMaxBackwardSpeed(), maxBackwardSpeed, epsilon);
        assertEquals(w.getCurrentMaxForwardSpeed(), maxForwardSpeed, epsilon);
        assertEquals(w.getCurrentBackwardsFriction(), roadFriction, epsilon);
        assertEquals(w.getCurrentMaxLateralImpulse(), maxLateralImpulse, epsilon);


    }

    @Test
    public void cpyTest() throws Exception{
        float driveForce = 2;
        float maxLateralImpulse = 3;
        float maxForwardSpeed = 4;
        float maxBackwardSpeed = 5;
        float roadFriction = 6;


        World world = new World(new Vector2(0, 0), true);
        float width = 10;
        float height = 20;
        float density = 1;



        Wheel w = new Wheel(world, width, height, density);
        w.setCharacteristics(driveForce, maxLateralImpulse, maxForwardSpeed, maxBackwardSpeed, roadFriction);


        Wheel copy = w.cpy();

        copy.updateValues();
        assertEquals(copy.getCurrentMaxLateralImpulse(), maxLateralImpulse, epsilon);
        assertEquals(copy.getCurrentBackwardsFriction(), roadFriction, epsilon);
        assertEquals(copy.getCurrentMaxForwardSpeed(), maxForwardSpeed, epsilon);
        assertEquals(copy.getCurrentMaxBackwardSpeed(), maxBackwardSpeed, epsilon);
        assertEquals(copy.getDriveForce(), driveForce, epsilon);




    }

}
