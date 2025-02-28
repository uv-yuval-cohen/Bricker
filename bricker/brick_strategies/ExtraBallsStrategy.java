package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.PuckBall;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * a class for an additional strategy where 2 extra balls called mock balls appear in the game
 */
public class ExtraBallsStrategy extends CollisionStrategyDecorator {
    private static final float MOCKBALL_RADIUS = 15; // the radius of the mock ball
    private final GameObjectCollection gameObjects; // all the objects in the game
    private final Renderable ballRenderable; // the renderable that renders a mock ball
    private final Sound collisionSound; // the collision sound
    private static final float BALL_SPEED = 200; // the mock ball speed
    private final Vector2 windowDimensions;

    /**
     * A constructor for the extra balls strategy
     * @param wrappedStrategy - the base strategy that this strategy is additional to
     * @param gameObjects - all the objects in the game
     * @param ballRenderable - the renderable that renders a mock ball
     * @param collisionSound - the collision sound
     * @param windowDimensions The dimensions of the game window.
     */
    public ExtraBallsStrategy(CollisionStrategy wrappedStrategy, GameObjectCollection gameObjects,
                              Renderable ballRenderable, Sound collisionSound, Vector2 windowDimensions) {
        super(wrappedStrategy);
        this.gameObjects = gameObjects;
        this.ballRenderable = ballRenderable;
        this.collisionSound = collisionSound;
        this.windowDimensions = windowDimensions;
    }

    /**
     *  what happens when the game object collides with other game object
     * @param brick - a brick object
     * @param otherObj - the other object that collides the brick
     */
    @Override
    public void onCollision(GameObject brick, GameObject otherObj) {
        super.onCollision(brick, otherObj); // Execute basic behavior (remove brick, decrement counter)

        // Get the center of the brick for spawning pucks
        Vector2 brickCenter = brick.getCenter();
        Vector2 puckDimensions = new Vector2(MOCKBALL_RADIUS,MOCKBALL_RADIUS);

        // Generate two new pucks
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            double angle = random.nextDouble() * Math.PI; // Random angle in radians
            float ballVelX = (float) Math.cos(angle) * BALL_SPEED;
            float ballVelY = (float) Math.sin(angle) * BALL_SPEED;

            // Create a new puck using the renderable and collision sound
            GameObject puck = new PuckBall(
                    brickCenter,
                    puckDimensions,
                    ballRenderable,
                    collisionSound, // Use the provided sound
                    gameObjects,
                    windowDimensions
            );

            // Set the puck's velocity
            puck.setVelocity(new Vector2(ballVelX, ballVelY));

            // Add the puck to the game
            gameObjects.addGameObject(puck);
        }
    }
}
