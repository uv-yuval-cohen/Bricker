package bricker.brick_strategies;

import bricker.gameobjects.FallingHeart;
import bricker.gameobjects.HealthBar;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class for an additional collision strategy where a heart falls.
 */
public class FallingHeartStrategy extends CollisionStrategyDecorator{
    private static final int HEART_RADIUS = 20; // heart radius
    private final GameObject mainPaddle; // the main paddle in the game
    private final Renderable heartRenderable; // a renderable that renders a heart
    private final HealthBar healthBar; // the game's health bar
    private final GameObjectCollection gameObjects; // all the objects in the game
    private static final int HEART_VELX = 0; // falling heart velocity in X axis
    private static final int HEART_VELY= 100; // falling heart velocity in Y axis

    /**
     * A constructor of the strategy
     * @param wrappedStrategy - the base strategy that this strategy is additional to
     * @param mainPaddle - the main paddle in the game
     * @param heartRenderable - a renderable that renders a heart
     * @param gameObjects - all the objects in the game
     * @param healthBar - the game's health bar
     */
    public FallingHeartStrategy(CollisionStrategy wrappedStrategy, GameObject mainPaddle,
                                Renderable heartRenderable, GameObjectCollection gameObjects, HealthBar healthBar) {
        super(wrappedStrategy);
        this.healthBar = healthBar;
        this.mainPaddle = mainPaddle;
        this.heartRenderable = heartRenderable;
        this.gameObjects = gameObjects;
    }

    /**
     *  what happens when the game object collides with other game object
     * @param brick - a brick object
     * @param otherObj - the other object that collides the brick
     */
    @Override
    public void onCollision(GameObject brick, GameObject otherObj) {
        super.onCollision(brick, otherObj);
        //get the center of the brick
        Vector2 brickCenter = brick.getCenter();

        // create a heart in the center of the brick that will fall down
        FallingHeart heart = new FallingHeart(brickCenter, new Vector2(HEART_RADIUS,HEART_RADIUS),
                heartRenderable, mainPaddle,heartRenderable, gameObjects, healthBar);
        gameObjects.addGameObject(heart);
        //give heart velocity
        heart.setVelocity(new Vector2(HEART_VELX, HEART_VELY));



    }
}
