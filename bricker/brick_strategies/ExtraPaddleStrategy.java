package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * a class for an additional strategy where an extra paddle appears in the game
 */
public class ExtraPaddleStrategy extends CollisionStrategyDecorator {
    private final GameObjectCollection gameObjects; // the objects in the game
    private final Renderable paddleRenderable; // the renderable that renders the extra paddle
    private final Vector2 windowDimensions; // the dimesions of the window in the game
    private final UserInputListener inputListener; // listens to the playes input for
    // moving the paddle
    public static GameObject currentExtraPaddle = null; // Static field to track the single
    // extra paddle
    private static final int EXTRA_PADDLE_WIDTH = 100;
    private static final int EXTRA_PADDLE_HEIGHT = 15;

    /**
     * A constructor for the extra paddle strategy
     * @param toBeDecorated -the base strategy that this strategy is additional to
     * @param gameObjects - all the objects in the game
     * @param paddleRenderable - the renderable that renders a paddle
     * @param windowDimensions  - the dimensions of the games' window
     * @param inputListener -  listens to the plays input for moving the paddle
     */
    public ExtraPaddleStrategy(CollisionStrategy toBeDecorated, GameObjectCollection gameObjects,
                               Renderable paddleRenderable, Vector2 windowDimensions, UserInputListener inputListener) {
        super(toBeDecorated);
        this.gameObjects = gameObjects; // all the objects in the game
        this.paddleRenderable = paddleRenderable; // the renderable that renders a paddle
        this.windowDimensions = windowDimensions; // the dimensions of the games' window
        this.inputListener = inputListener; // listens to the plays input for moving the paddle
    }

    @Override
    /**
     *  what happens when the game object collides with other game object
     * @param thisObj - the first object
     * @param otherObj - the other object that collides the first object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        // Check if there's already an active extra paddle
        if (currentExtraPaddle != null) {
            return; // Do nothing if there's already an extra paddle
        }

        // Create the extra paddle
        Vector2 paddleSize = new Vector2(EXTRA_PADDLE_WIDTH, EXTRA_PADDLE_HEIGHT);
        Vector2 paddlePosition = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        ExtraPaddle extraPaddle = new ExtraPaddle(paddlePosition, paddleSize, paddleRenderable, inputListener, this, false);

        // Add the paddle to the game and update the static field
        currentExtraPaddle = extraPaddle;
        gameObjects.addGameObject(extraPaddle);
    }

    /**
     * removes the extra paddle from the game
     */
    public void removeExtraPaddle() {
        if (currentExtraPaddle != null) {
            gameObjects.removeGameObject(currentExtraPaddle); // Remove the paddle from the game
            currentExtraPaddle = null; // Reset the field
        }
    }

}


