package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for a collision of game objects in the game
 */
public interface CollisionStrategy {
    /**
     * what happens when the game object collides with other game object
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
