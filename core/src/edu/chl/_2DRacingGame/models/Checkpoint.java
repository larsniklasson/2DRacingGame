package edu.chl._2DRacingGame.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public class Checkpoint {

    private final CheckpointType type;
    private final List<CheckpointDirection> allowedPassingDirections = new ArrayList<>();

    private final World world;
    private final Body body;

    public Checkpoint(Shape shape, World world) {
        this(shape, CheckpointType.INVISIBLE, world);
    }

    // TODO inject body?
    public Checkpoint(Shape shape, CheckpointType type, World world) {
        this.type = type;
        this.world = world;
        this.body = createBody(shape);
    }

    private Body createBody(Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.getFixtureList().first().setSensor(true);
        body.setUserData(this);

        return body;
    }

    public void addAllowedPassingDirection(CheckpointDirection direction) {
        allowedPassingDirections.add(direction);
    }

    public boolean isValidEntry(Vector2 beginContactPosition, Vector2 endContactPosition) {
        for (CheckpointDirection direction : allowedPassingDirections) {
            if (direction.isValidEntry(beginContactPosition, endContactPosition)) {
                return true;
            }
        }

        return false;
    }

    public CheckpointType getType() {
        return type;
    }

}