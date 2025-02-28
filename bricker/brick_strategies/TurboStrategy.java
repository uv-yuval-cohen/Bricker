package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;

/**
 * A class for the turbo strategy where the ball changes its image and velocity.
 */
public class TurboStrategy extends CollisionStrategyDecorator{
    private final Ball mainBall;
    private final Renderable redBallRenderable;
    private static final float TURBO_VELOCITY_CHANGE_FACTOR = 1.4f;
    private static final int MAX_COLLISION = 6;




    /**
     * creates a collision strategy decorator
     *
     * @param wrappedStrategy - the base strategy that the strategy decorator is additional to
     */
    public TurboStrategy(CollisionStrategy wrappedStrategy, Ball mainBall, Renderable redBallRenderable){
        super(wrappedStrategy);
        this.mainBall = mainBall;


        this.redBallRenderable = redBallRenderable;
    }

    /**
     * What happens when the ball hits the brick with this strategy.
     * @param thisObj - the first object
     * @param otherObj - the other object that collides the first object
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if(otherObj == mainBall && !mainBall.getTurboState()) {
                //update ball velocity
                mainBall.turboState(TURBO_VELOCITY_CHANGE_FACTOR, redBallRenderable, MAX_COLLISION);
        }
    }
}
