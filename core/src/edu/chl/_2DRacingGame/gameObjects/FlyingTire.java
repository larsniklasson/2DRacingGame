package edu.chl._2DRacingGame.gameObjects;

import com.badlogic.gdx.physics.box2d.World;
import edu.chl._2DRacingGame.mapobjects.GroundMaterial;

/**
 * Subclass to Tire used to avoid adding GroundMaterials. (for flying vehicles)
 * Created specifically for the MagicCarpet vehicle.
 *
 * @author Lars Niklasson
 */
public class FlyingTire extends Tire{


    public FlyingTire(World world, float width, float height, float density) {
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
