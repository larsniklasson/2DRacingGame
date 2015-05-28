package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Daniel Sunnerberg
 */
public class CheckpointFactory {

    /**
     * Creates a checkpoint and binds it to the specified body in the passed world.
     *
     * @param world
     * @param shape
     * @param type
     * @return
     */
    public static Checkpoint createCheckpoint(World world, Shape shape, CheckpointType type) {
        Body checkpointBody = BodyFactory.createStaticBody(world, shape);
        Checkpoint checkpoint = new Checkpoint(type);
        checkpointBody.getFixtureList().first().setSensor(true);
        checkpointBody.setUserData(checkpoint);
        return checkpoint;
    }
}
