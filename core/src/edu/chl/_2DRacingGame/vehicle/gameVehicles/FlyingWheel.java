package edu.chl._2DRacingGame.vehicle.gameVehicles;

import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * Subclass to Wheel used to avoid adding GroundMaterials. (for flying vehicles)
 * Created specifically for the MagicCarpet vehicle.
 *
 * @author Lars Niklasson
 */
public class FlyingWheel extends Wheel {


    public FlyingWheel(World world, float width, float height, float density) {
        super(world, width, height, density);
    }

    @Override
    public void addGroundMaterial(GroundMaterial gm){
        //do nothing
    }

    @Override
    public void removeGroundMaterial(GroundMaterial gm){
        //do nothing
    }
}
