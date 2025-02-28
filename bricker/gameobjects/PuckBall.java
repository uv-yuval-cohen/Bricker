package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a puck-like ball in the Bricker game.
 * The puck bounces off objects, flipping its velocity upon collision,
 * and plays a collision sound if provided.
 */
public class PuckBall extends GameObject {
    private final Sound collisionSound;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;

    /**
     * Constructor for the PuckBall class.
     *
     * @param topLeftCorner   Position of the object, in window coordinates (pixels).
     * @param dimensions      Width and height in window coordinates.
     * @param renderable      The renderable representing the object.
     * @param collisionSound  The sound to play on collisions.
     * @param gameObjects     The collection of game objects.
     * @param windowDimensions The dimensions of the game window.
     */
    public PuckBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                    GameObjectCollection gameObjects, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Updates the puck ball's state. Removes the puck ball from the game
     * if it moves outside the vertical bounds of the game window.
     *
     * @param deltaTime Time elapsed since the last frame (in seconds).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Check if the ball is outside the screen bounds
        if (getCenter().y() < 0 || getCenter().y() > windowDimensions.y() ) {
            gameObjects.removeGameObject(this); // Remove from gameObjects
        }
    }

    /**
     * Handles behavior when the puck collides with another object.
     * Flips the velocity vector based on the collision normal and plays a sound if available.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Collision details, including the normal of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Flip the velocity on collision
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);

        // Play collision sound if available
        if (collisionSound != null) {
            collisionSound.play();
        }
    }
}
