package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * The basic collision strategy in the game. Where the object disappears after it collides
 * with the other object.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjects; // all the objects in the game
    private final Counter brickCounter; //a counter of the current amount of bricks in the wall

    /**
     * Constructor for the basic collision strategy.
     * @param gameObjects - all the objects in the game
     * @param brickCounter - a counter of the current amount of bricks in the wall
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }

    /**
     * implements what happens when the game object collides with other game object
     * @param thisObj - the first object
     * @param otherObj - the other object that collides the first object
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS) ) {
            brickCounter.decrement(); // Decrement the counter
        }

    }

}
