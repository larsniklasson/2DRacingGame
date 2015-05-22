package edu.chl._2DRacingGame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chl._2DRacingGame.gameObjects.Trackable;
import edu.chl._2DRacingGame.mapobjects.TrackSection;
import edu.chl._2DRacingGame.gameObjects.Vehicle;
import edu.chl._2DRacingGame.models.Checkpoint;

/**
 * Created by Lasse on 2015-04-24.
 * Revised by Daniel Sunnerberg on 2015-04-26.
 * Revised by Lars Niklasson on 2015-05-03.
 */
class ContactController implements ContactListener {

    private final ContactDelegator contactDelegator;

    private Vector2 beginContactPosition;

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
            beginContactPosition = ((Vehicle) b.getUserData()).getPosition().cpy();
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
            Checkpoint checkpoint = (Checkpoint) a.getUserData();
            Vehicle vehicle = (Vehicle) b.getUserData();

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
