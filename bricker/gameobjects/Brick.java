package bricker.gameobjects;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a brick in the Bricker game.
 * Each brick has an associated collision strategy that defines its behavior when hit.
 */
public class Brick extends GameObject {

    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a Brick object.
     *
     * @param topLeftCorner   Position of the brick, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height of the brick, in window coordinates.
     * @param renderable      The renderable representing the brick. Can be null, in which case
     *                        the brick will not be rendered.
     * @param collisionStrategy The collision strategy that defines the behavior of the brick when hit.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Handles behavior when the brick is hit by another object.
     * Delegates collision handling to the associated collision strategy.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Collision details, including the normal of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

}
