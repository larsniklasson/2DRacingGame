package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * Created by Lasse on 2015-04-24.
 * Revised by Daniel Sunnerberg on 2015-04-26.
 */
public class ContactController implements ContactListener {

    private final ContactDelegator contactDelegator;

    private Vector2 beginContactPosition;

    public ContactController(ContactDelegator contactDelegator) {
        this.contactDelegator = contactDelegator;
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() instanceof Tire && b.getUserData() instanceof TrackSection) {
            ((Tire) a.getUserData()).grounds.add(((TrackSection) b.getUserData()).getGroundMaterial());
        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire) {
            ((Tire) b.getUserData()).grounds.add(((TrackSection) a.getUserData()).getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Car) {
            beginContactPosition = ((Car) b.getUserData()).body.getPosition().cpy();
        }

    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() instanceof Tire && b.getUserData() instanceof TrackSection) {
            ((Tire) a.getUserData()).grounds.remove(((TrackSection) b.getUserData()).getGroundMaterial());
        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire) {
            ((Tire) b.getUserData()).grounds.remove(((TrackSection) a.getUserData()).getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Car) {
            Checkpoint checkpoint = (Checkpoint) a.getUserData();
            Car car = (Car) b.getUserData();

            processCheckpointCollision(checkpoint, car);
        }
    }

    private void processCheckpointCollision(Checkpoint checkpoint, Car car) {
        boolean isValidEntry = checkpoint.isValidEntry(beginContactPosition, car.body.getPosition().cpy());
        contactDelegator.enteredCheckpoint(car, checkpoint, isValidEntry);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
