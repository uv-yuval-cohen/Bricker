package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Describes the API of the additional strategies.
 */
public abstract class CollisionStrategyDecorator implements CollisionStrategy {
    private final CollisionStrategy wrappedStrategy;

    /**
     * creates a collision strategy decorator
     * @param wrappedStrategy - the base strategy that the strategy decorator is additional to
     */
    public CollisionStrategyDecorator(CollisionStrategy wrappedStrategy) {
        this.wrappedStrategy = wrappedStrategy;
    }

    /**
     *  what happens when the game object collides with other game object
     * @param thisObj - the first object
     * @param otherObj - the other object that collides the first object
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        wrappedStrategy.onCollision(thisObj, otherObj); // Delegate to the wrapped strategy
    }
}
