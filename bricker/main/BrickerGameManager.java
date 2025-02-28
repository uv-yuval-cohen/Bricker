package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;


/**
 * Main game manager for the Bricker game.
 * Responsible for initializing game objects, managing game state,
 * and handling win/lose conditions.
 */
public class BrickerGameManager extends GameManager {

    // Wall and border constants
    private static final int BORDER_WIDTH = 10;
    private static final Color BORDER_COLOR = new Color(255, 165, 0);
    // Paddle constants
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final float PADDLE_INITIAL_POSITION_Y = 30;

    // Ball constants
    private static final float BALL_SPEED = 200;
    private static final float BALL_RADIUS = 20;

    // Brick layout constants
    private static final int NUM_ROWS = 5; // Number of brick rows
    private static final int NUM_COLS = 8; // Number of brick columns
    private static final int BRICK_MARGIN = 5; // Space between bricks
    private static final int BRICK_HEIGHT = 15; // Brick height in pixels

    // Health bar constants
    private static final int HEART_RADIUS = 20;

    private static final float CENTER_FACTOR = 0.5f;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_LEFT = -1;

    private static final float WINDOW_WIDTH = 700;
    private static final float WINDOW_HEIGHT = 500;
    private static final float BRICK_WIDTH = (WINDOW_WIDTH - 2 * BORDER_WIDTH
                                            - (BRICK_MARGIN * (NUM_COLS - 1))) / NUM_COLS;

    private Ball ball; // Main game ball
    private Vector2 windowDimensions; // Dimensions of the game window
    private WindowController windowController; // Handles window operations
    private Counter brickCounter; // Tracks remaining bricks
    private HealthBar healthBar; // Manages the player's health bar

    /**
     * Constructs the game manager with the specified title and window dimensions.
     *
     * @param windowTitle Title of the game window.
     * @param windowDimensions Dimensions of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {

        super(windowTitle, windowDimensions);
    }

    /**
     * Called once per frame. Updates the game state, checks for win/lose conditions.
     *
     * @param deltaTime Time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();

    }

    /**
     * Initializes the game by setting up all necessary components, including the game window,
     * paddle, ball, bricks, and background. This method is called when the game is first launched.
     *
     * @param imageReader      Reads image assets for game objects.
     * @param soundReader      Reads sound assets for game objects.
     * @param inputListener    Listens for user input during gameplay.
     * @param windowController Controls the game window and its properties.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        // Initialize the game counter for bricks
        brickCounter = new Counter(0); // Start the counter at 0
        // Create health bar
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        healthBar = new HealthBar(heartImage, HEART_RADIUS, gameObjects());
        // Retrieve window dimensions
        windowDimensions = windowController.getWindowDimensions();
        // Create and initialize the main ball
        createBall(imageReader,soundReader);
        // Create user paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = createPaddle(imageReader,inputListener);
        //creating walls
        createWalls();
        //create background
        GameObject background = createBackground(imageReader);
        //camera support
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        //creating bricks
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);

        // Use a factory to create strategies for brick behavior
        Renderable mockBallImage = imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        //Turbo ball image
        Renderable turboBallImage = imageReader.readImage("assets/redball.png", true);
        StrategyFactory strategyFactory = new StrategyFactory(
                gameObjects(),
                mockBallImage,         // Renderable for mock ball
                collisionSound,        // Collision sound for bricks
                paddleImage,           // Paddle renderable
                windowDimensions,      // Game window dimensions
                inputListener,         // User input
                ball,                  // Main ball object
                paddle,                // Paddle object
                heartImage,            // Heart image for health bar
                healthBar,             // Health bar object
                turboBallImage

        );
        createBrickLayout(windowController, brickImage, strategyFactory);
    }

    /**
     * Checks if the player has won or lost the game. Handles win/lose logic and prompts
     * the player to restart or close the game.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt= "";
        if (brickCounter.value() == 0 ) {
            prompt = "You Won!";

        }
        if (ballHeight > windowDimensions.y()) {
            healthBar.removeLife();
            if (healthBar.getLives() > 0) {
                resetBall(); // Reset ball if lives are remaining
                return;
            } else {
                prompt = "You Lost!";
            }
        }
        if (!prompt.isEmpty()) {

            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                // reset by danogl doesnt reset it..needs manual reset
                ExtraPaddleStrategy.currentExtraPaddle=null;
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Creates a grid layout of bricks on the game window.
     * @param windowController - windowController
     * @param brickImage - image of the brick in the game
     * @param  factory - a factory for creating a strategy.
     */
    private void createBrickLayout(WindowController windowController,
                                   Renderable brickImage, StrategyFactory factory) {
        // Calculate brick width, accounting for borders and margins
        float brickWidth = BRICK_WIDTH;

        float startingY = BRICK_MARGIN + PADDLE_HEIGHT;

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                float xPosition = BORDER_WIDTH + col * (brickWidth + BRICK_MARGIN);
                float yPosition = startingY + row * (BRICK_HEIGHT + BRICK_MARGIN);

                Vector2 position = new Vector2(xPosition, yPosition);
                Vector2 dimensions = new Vector2(brickWidth, BRICK_HEIGHT);

                createBrick(position, dimensions, brickImage, factory);
            }
        }
    }

    /**
     * creates a single wall in the game
     * @param position - the position of the wall
     * @param size - size of the wall
     */
    private void createWall(Vector2 position, Vector2 size) {
        gameObjects().addGameObject(new GameObject(position,
                size,
                new RectangleRenderable(BORDER_COLOR)));
    }

    /**
     * Creates a single brick and provides it a strategy
     * @param position - brick position
     * @param dimensions - brick dimensions
     * @param brickImage - brick image
     * @param strategyFactory - a factory for creating a strategy
     */
    private void createBrick(Vector2 position, Vector2 dimensions,
                             Renderable brickImage, StrategyFactory strategyFactory) {
        brickCounter.increment();

        // Start with the basic strategy
        CollisionStrategy strategy = new BasicCollisionStrategy(gameObjects(), brickCounter);
        strategy = strategyFactory.createStrategy(strategy);
        // Create the brick with the composed strategy
        Brick brick = new Brick(position, dimensions, brickImage, strategy);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }

    /**
     * reset ball to be back in the game's boundaries
     */
    private void resetBall() {
        initializeBall(ball); // Reuse helper method
    }

    /**
     * Sets the ball's center and velocity.
     * @param ball The ball object to update.
     */
    private void initializeBall(Ball ball) {
        ball.setCenter(windowDimensions.mult(CENTER_FACTOR)); // Center the ball
        Random random = new Random();
        float ballVelX = BALL_SPEED * (random.nextBoolean() ? DIRECTION_RIGHT : DIRECTION_LEFT);
        float ballVelY = BALL_SPEED * (random.nextBoolean() ? DIRECTION_RIGHT : DIRECTION_LEFT);
        ball.setVelocity(new Vector2(ballVelX, ballVelY)); // Set randomized velocity
    }

    private Ball createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");

        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS),
                ballImage, collisionSound, this);
        initializeBall(ball); // Reuse helper method
        gameObjects().addGameObject(ball);
        return ball;
    }

    private GameObject createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, true);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_INITIAL_POSITION_Y));
        gameObjects().addGameObject(paddle);
        return paddle;
    }


    private void createWalls() {
        createWall(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y())); // Left
        createWall(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y())); // Right
        createWall(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_WIDTH)); // Top
    }

    private GameObject createBackground(ImageReader imageReader) {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false)
        );
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        return background;
    }

    /**
     * The entry point of the program. Initializes and starts the Bricker game.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {

        new BrickerGameManager("Bricker", new Vector2(WINDOW_WIDTH,WINDOW_HEIGHT)).run();
    }


}

