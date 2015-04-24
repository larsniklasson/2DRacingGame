package edu.chl._2DRacingGame.helperClasses;

import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.world.GameWorld;

/**
 * Created by Lasse on 2015-04-24.
 */
public class MyContactListener implements ContactListener{



    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if(a.getUserData() instanceof Tire && b.getUserData() instanceof TrackSection){
            ((Tire)a.getUserData()).grounds.add( ((TrackSection)b.getUserData()).getGroundMaterial() );
        }

        if(a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire){
            ((Tire)b.getUserData()).grounds.add(((TrackSection) a.getUserData()).getGroundMaterial() );
        }





    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if(a.getUserData() instanceof Tire && b.getUserData() instanceof TrackSection){
            ((Tire)a.getUserData()).grounds.remove( ((TrackSection)b.getUserData()).getGroundMaterial() );
        }

        if(a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire){
            ((Tire)b.getUserData()).grounds.remove(((TrackSection) a.getUserData()).getGroundMaterial());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
