package bricker.brick_strategies;

import danogl.GameObject;

public class DoubleBehaviorStrategy implements CollisionStrategy {
    private final CollisionStrategy combinedStrategy;


    /**
     * Constructor for DoubleBehaviorStrategy.
     *
     * @param combinedStrategy The combined strategy that encapsulates all behaviors -
     *                         Provided by the factory to ensure correct wrapping.
     */
    public DoubleBehaviorStrategy(CollisionStrategy combinedStrategy) {
        this.combinedStrategy = combinedStrategy;
    }


    /**
     * Handles the collision by delegating to the combined strategy.
     *
     * @param thisObj  The object involved in the collision.
     * @param otherObj The other object that collided with thisObj.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Delegate to the combined strategy
        combinedStrategy.onCollision(thisObj, otherObj);
    }

}