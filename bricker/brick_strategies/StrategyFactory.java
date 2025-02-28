package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.HealthBar;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A factory class for creating collision strategies in the Bricker game.
 * Responsible for dynamically adding behaviors to bricks when a collision occurs.
 */
public class StrategyFactory {
    private final GameObjectCollection gameObjects; // al the objects in the game
    private final Renderable ballRenderable; //renders a ball
    private final Sound ballCollisionSound; // the sound of the collision
    private final Renderable paddleRenderable; // a renderable for the paddle
    private final Vector2 windowDimensions; // the dimensions of the game's window
    private final UserInputListener inputListener; // an input listener
    private final Ball mainBall; // the main ball in the game

    private final GameObject mainPaddle; // the main paddle in the game
    private final Renderable heartRenderable; // renders a heart
    private final HealthBar healthBar; // the game's health bar
    private final Renderable turboBallRenderable;

    private static final double SPECIAL_BEHAVIOR_THRESHOLD = 0.5;
    private static final int MAX_SPECIAL_BEHAVIORS = 3;
    private static final int INITIAL_SPECIAL_BEHAVIORS = 2;
    private static final int ALL_SPECIAL_STRATEGIES = 5;
    private static final int SINGLE_SPECIAL_STRATEGIES = 4;
    private static final int EXTRA_BALL_INDEX = 1;
    private static final int EXTRA_PADDLE_INDEX = 2;
    private static final int TURBO_INDEX = 3;
    private static final int FALLING_HEART_INDEX = 4;



    /**
     * Constructs a factory for creating collision strategies in the Bricker game.
     *
     * @param gameObjects       The collection of game objects in the game.
     * @param ballRenderable    The renderable object for the ball.
     * @param ballCollisionSound The sound effect for ball collisions.
     * @param paddleRenderable  The renderable object for the paddle.
     * @param windowDimensions  The dimensions of the game window.
     * @param inputListener     Listener for user input.
     * @param mainBall          The main ball object in the game.
     * @param mainPaddle        The main paddle object in the game.
     * @param heartRenderable   The renderable object for the health indicator.
     * @param healthBar         The health bar object in the game.
     */
    public StrategyFactory(GameObjectCollection gameObjects, Renderable ballRenderable, Sound ballCollisionSound,
                           Renderable paddleRenderable, Vector2 windowDimensions, UserInputListener inputListener,
                           Ball mainBall,
                           GameObject mainPaddle, Renderable heartRenderable, HealthBar healthBar, Renderable turboBallRenderable) {
        this.gameObjects = gameObjects;
        this.ballRenderable = ballRenderable;
        this.ballCollisionSound = ballCollisionSound;
        this.paddleRenderable = paddleRenderable;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.mainBall = mainBall;
        this.mainPaddle = mainPaddle;
        this.heartRenderable = heartRenderable;
        this.healthBar = healthBar;
        this.turboBallRenderable = turboBallRenderable;
    }

    /**
     * Creates a collision strategy for a brick.
     * Randomly decides whether to add a special behavior or return the base strategy.
     *
     * @param baseStrategy The initial collision strategy to build upon.
     * @return A collision strategy with optional special behaviors.
     */
    public CollisionStrategy createStrategy(CollisionStrategy baseStrategy) {

        if (Math.random() > SPECIAL_BEHAVIOR_THRESHOLD) {
            return baseStrategy; // No special behavior
        }

        // Determine the type of special behavior to apply
        StrategyType strategyType = getRandomSpecialStrategyType(true); // Include double behavior
        return createByStrategyType(strategyType, baseStrategy);
    }

    /**
     * Creates a strategy with multiple combined behaviors.
     *
     * @param strategy The base strategy to enhance.
     * @return A collision strategy with combined behaviors.
     */
    private CollisionStrategy createDoubleStrategy(CollisionStrategy strategy) {
        int requiredSpecialBehaviors = INITIAL_SPECIAL_BEHAVIORS; // Total behaviors required
        int achievedSpecialBehaviors = 0; // Behaviors added so far
         while (achievedSpecialBehaviors < requiredSpecialBehaviors) {

             StrategyType strategyType = getRandomSpecialStrategyType(requiredSpecialBehaviors< MAX_SPECIAL_BEHAVIORS);
             if (strategyType == StrategyType.DOUBLE_BEHAVIOR) {
                 // another double behavior was chosen
                 requiredSpecialBehaviors++;
                 continue;
             }

             // Add the behavior
             strategy = createByStrategyType(strategyType, strategy);
             achievedSpecialBehaviors++;

         }
         return new DoubleBehaviorStrategy(strategy);
    }

    /**
     * Randomly selects a strategy type based on probabilities.
     *
     * @param includeDoubleBehavior If true, allows DOUBLE_BEHAVIOR as a valid selection.
     * @return The selected strategy type.
     */
    private StrategyType getRandomSpecialStrategyType(boolean includeDoubleBehavior) {
        double rand = Math.random();
        int totalStrategies = includeDoubleBehavior ? ALL_SPECIAL_STRATEGIES : SINGLE_SPECIAL_STRATEGIES; // Total strategies based on the flag
        double probabilityStep = 1.0 / totalStrategies; // Divide range [0,1) equally

        if (rand < probabilityStep * EXTRA_BALL_INDEX) {
            return StrategyType.EXTRA_BALLS;
        } else if (rand < probabilityStep * EXTRA_PADDLE_INDEX) {
            return StrategyType.EXTRA_PADDLE;
        } else if (rand < probabilityStep * TURBO_INDEX) {
            return StrategyType.TURBO;
        } else if (rand < probabilityStep * FALLING_HEART_INDEX) {
            return StrategyType.FALLING_HEART;
        } else {
            return StrategyType.DOUBLE_BEHAVIOR; // Only reached if includeDoubleBehavior == true
        }
    }


    private CollisionStrategy createExtraBallsStrategy(CollisionStrategy baseStrategy) {
        return new ExtraBallsStrategy(baseStrategy, gameObjects, ballRenderable, ballCollisionSound, windowDimensions);
    }

    private CollisionStrategy createExtraPaddleStrategy(CollisionStrategy baseStrategy) {
        return new ExtraPaddleStrategy(baseStrategy, gameObjects, paddleRenderable, windowDimensions, inputListener);
    }

    private CollisionStrategy createTurboStrategy(CollisionStrategy baseStrategy) {
        return new TurboStrategy(baseStrategy, mainBall, turboBallRenderable);
    }

    private CollisionStrategy createFallingHeartStrategy(CollisionStrategy baseStrategy) {
        return new FallingHeartStrategy(baseStrategy, mainPaddle, heartRenderable, gameObjects, healthBar);
    }

    private CollisionStrategy createByStrategyType(StrategyType strategyType, CollisionStrategy baseStrategy) {
        switch (strategyType) {
            case EXTRA_BALLS:
                return createExtraBallsStrategy(baseStrategy);
            case EXTRA_PADDLE:
                return createExtraPaddleStrategy(baseStrategy);
            case TURBO:
                return createTurboStrategy(baseStrategy);
            case FALLING_HEART:
                return createFallingHeartStrategy(baseStrategy);
            case DOUBLE_BEHAVIOR:
                return createDoubleStrategy(baseStrategy); // Handle double behavior
            default:
                return baseStrategy;
        }
    }


}
