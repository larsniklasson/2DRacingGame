package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.scenes.scene2d.Actor;

/** Interface to ensure an object or entity
 * has an actor.
 *
 * @author Lars Niklasson
 */
public interface Actable {
    /**
     *
     * @return The actor of this entity
     */
    Actor getActor();

}
