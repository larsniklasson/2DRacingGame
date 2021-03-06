package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.vehicle.Trackable;
import edu.chl._2DRacingGame.map.objects.TrackSection;
import edu.chl._2DRacingGame.models.Vehicle;
import edu.chl._2DRacingGame.map.objects.Checkpoint;

/**
 * A controller which is responsible for handling logic related to collisions.
 *
 * Created by Lasse on 2015-04-24.
 * Revised by Daniel Sunnerberg on 2015-04-26.
 * Revised by Lars Niklasson on 2015-05-03.
 */
class ContactController implements ContactListener {

    private final ContactDelegator contactDelegator;

    private Vector2 beginContactPosition;

    /**
     * Creates a new ContactController which will delegate specific events to the delegate.
     *
     * @param contactDelegator delegate who wishes to be notified about specific events
     */
    public ContactController(ContactDelegator contactDelegator) {
        this.contactDelegator = contactDelegator;
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() instanceof Trackable && b.getUserData() instanceof TrackSection) {
            Trackable trackable = (Trackable)a.getUserData();
            TrackSection ts = (TrackSection)b.getUserData();

            trackable.addGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Trackable) {
            Trackable trackable = (Trackable)b.getUserData();
            TrackSection ts = (TrackSection)a.getUserData();

            trackable.addGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) b.getUserData();
            if (! vehicle.isControlledByClientPlayer()) {
                return;
            }
            beginContactPosition = vehicle.getPosition().cpy();
        }

    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() instanceof Trackable && b.getUserData() instanceof TrackSection) {

            Trackable trackable = (Trackable)a.getUserData();
            TrackSection ts = (TrackSection)b.getUserData();

            trackable.removeGroundMaterial(ts.getGroundMaterial());

        }

        if (a.getUserData() instanceof TrackSection && b.getUserData() instanceof Trackable) {
            Trackable trackable = (Trackable)b.getUserData();
            TrackSection ts = (TrackSection)a.getUserData();

            trackable.removeGroundMaterial(ts.getGroundMaterial());
        }

        if (a.getUserData() instanceof Checkpoint && b.getUserData() instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) b.getUserData();
            if (! vehicle.isControlledByClientPlayer()) {
                return;
            }
            Checkpoint checkpoint = (Checkpoint) a.getUserData();

            processCheckpointCollision(checkpoint, vehicle);
        }
    }

    private void processCheckpointCollision(Checkpoint checkpoint, Vehicle vehicle) {
        boolean isValidEntry = checkpoint.isValidEntry(beginContactPosition, vehicle.getPosition().cpy());
        contactDelegator.enteredCheckpoint(checkpoint, isValidEntry);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
