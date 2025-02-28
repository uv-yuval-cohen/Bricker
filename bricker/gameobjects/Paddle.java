package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle in the Bricker game.
 * The paddle is controlled by user input and moves horizontally within the game screen boundaries.
 */
public class Paddle extends GameObject {



    private static final float MOVEMENT_SPEED = 400;
    private static final float SCREEN_WIDTH = 682;
    private final UserInputListener inputListener;
    private static final float PADDLE_LEFT_BOUNDARY = 18;



    /**
     * Constructs a Paddle instance.
     *
     * @param topLeftCorner Position of the paddle in window coordinates (pixels).
     * @param dimensions    Width and height of the paddle in window coordinates.
     * @param renderable    The renderable representing the paddle.
     * @param inputListener The input listener for user keyboard controls.
     * @param isMainPaddle  Indicates whether this is the main paddle.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, boolean isMainPaddle) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;

    }

    /**
     * Updates the paddle's position based on user input and ensures it stays within the screen boundaries.
     *
     * @param deltaTime Time elapsed since the last frame (used for smooth movement).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        // Get current paddle position and dimensions
        Vector2 topLeftCorner = getTopLeftCorner() ;
        float paddleWidth = getDimensions().x();


        // Check boundaries for left and right keys
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && topLeftCorner.x() > PADDLE_LEFT_BOUNDARY) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && topLeftCorner.x() + paddleWidth < SCREEN_WIDTH) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }

        // Set paddle velocity based on allowed movement
        setVelocity(movementDir.mult(MOVEMENT_SPEED));


    }

}
