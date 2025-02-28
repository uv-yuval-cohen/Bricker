package bricker.gameobjects;

import bricker.brick_strategies.ExtraPaddleStrategy;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents an extra paddle in the Bricker game.
 * The paddle has a limited lifespan, defined by the number of collisions it can endure,
 * and is managed by an associated strategy.
 */
public class ExtraPaddle extends Paddle {
    private int collisionCount = 0; // Tracks the number of collisions
    private static final int MAX_COLLISIONS = 4; // Maximum collisions before removal
    private final ExtraPaddleStrategy strategy;



    /**
     * Constructs an ExtraPaddle instance.
     *
     * @param topLeftCorner Position of the paddle in window coordinates (pixels).
     * @param dimensions    Width and height of the paddle in window coordinates.
     * @param renderable    The renderable representing the paddle.
     * @param inputListener Listens to user input for controlling the paddle.
     * @param strategy      The strategy managing this paddle's behavior.
     * @param isMainPaddle  Indicates whether this is the main paddle.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, ExtraPaddleStrategy strategy, boolean isMainPaddle) {
        super(topLeftCorner, dimensions, renderable, inputListener, isMainPaddle);
        this.strategy = strategy;
    }

    /**
     * Handles collision events for the paddle.
     * Increments the collision count and removes the paddle via the strategy if the
     * maximum collision limit is reached.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, danogl.collisions.Collision collision) {
        super.onCollisionEnter(other, collision);

        // Increment collision count
        collisionCount++;

        // Check if the paddle reached its maximum collisions
        if (collisionCount >= MAX_COLLISIONS) {
            // Notify the strategy to handle paddle removal
            strategy.removeExtraPaddle();
        }
    }

    /**
     * Returns the current collision count for the paddle.
     *
     * @return The number of collisions the paddle has endured.
     */
    public int getCollisionCount() {

        return collisionCount; // Expose the collision count if needed
    }
}
