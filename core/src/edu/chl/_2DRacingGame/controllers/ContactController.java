package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.TrackSection;
import edu.chl._2DRacingGame.gameObjects.Car;
import edu.chl._2DRacingGame.gameObjects.Tire;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * Created by Lasse on 2015-04-24.
 * Revised by Daniel Sunnerberg on 2015-04-26.
 * Revised by Lars Niklasson on 2015-05-03.
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
            Tire tire = (Tire)a.getUserData();
            TrackSection ts = (TrackSection)b.getUserData();

            tire.addGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire) {
            Tire tire = (Tire)b.getUserData();
            TrackSection ts = (TrackSection)a.getUserData();

            tire.addGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Vehicle) {
            beginContactPosition = ((Vehicle) b.getUserData()).getBody().getPosition().cpy();
        }

    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() instanceof Tire && b.getUserData() instanceof TrackSection) {

            Tire tire = (Tire)a.getUserData();
            TrackSection ts = (TrackSection)b.getUserData();

            tire.removeGroundMaterial(ts.getGroundMaterial());

        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Tire) {
            Tire tire = (Tire)b.getUserData();
            TrackSection ts = (TrackSection)a.getUserData();

            tire.removeGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Car) {
            Checkpoint checkpoint = (Checkpoint) a.getUserData();
            Car car = (Car) b.getUserData();

            processCheckpointCollision(checkpoint, car);
        }
    }

    private void processCheckpointCollision(Checkpoint checkpoint, Car car) {
        boolean isValidEntry = checkpoint.isValidEntry(beginContactPosition, car.getBody().getPosition().cpy());
        contactDelegator.enteredCheckpoint(car, checkpoint, isValidEntry);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
