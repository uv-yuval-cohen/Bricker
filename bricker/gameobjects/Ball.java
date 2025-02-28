package bricker.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the Bricker game. The ball can bounce off surfaces, track the number of collisions,
 * and interact with the game manager (e.g., deactivate the camera after a certain number of collisions).
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter; // Counter to track collisions
    private final GameManager gameManager;
    private boolean counting = false;
    private boolean turboState = false;
    private int maxCollisionInTurbo;
    private float velocityChangeFactorInTurbo;
    private Renderable renderable;




    /**
     * Constructs a Ball object.
     *
     * @param topLeftCorner  Position of the ball, in window coordinates (pixels).
     * @param dimensions     Width and height of the ball, in window coordinates.
     * @param renderable     The renderable representing the ball.
     * @param collisionSound The sound to play on collision.
     * @param gameManager    The game manager for handling game-specific logic.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, GameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.gameManager = gameManager;

        this.collisionCounter = 0; // Initialize the counter
        this.renderable= renderable;
    }


    /**
     * Handles behavior when the ball collides with another object.
     * Flips the velocity vector, plays a collision sound, and tracks collisions.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Collision details, including the normal of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (counting) {
            collisionCounter ++;
        }
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();

        if(this.turboState && this.collisionCounter>=maxCollisionInTurbo){
            //reset ball state
            resetBallDesign();
            this.turboState = false;
            this.counting = false;
        }

    }

    /**
     * Activates collision tracking and resets the collision counter to zero.
     */
    public void countToMaxCollisions() {
        counting = true;
        collisionCounter = 0;
    }

    private void SetTurboState(boolean isTurbo){
        this.turboState = isTurbo;
    }
    public boolean getTurboState(){
        return turboState;
    }

    private void resetBallDesign(){
        //reset velocity
        this.setVelocity(this.getVelocity().mult((1/velocityChangeFactorInTurbo)));
        //reset ball image
        this.renderer().setRenderable(renderable);
    }

    /**
     * A function that initializes the ball's turbo state.
     * @param velocityChangeFactor - the change factor of the ball velocity.
     * @param turboBallRenderable - the renderable of the turbo state.
     * @param numberOfCollisions - number of collision in turbo.
     */
    public void turboState(float velocityChangeFactor, Renderable turboBallRenderable, int numberOfCollisions){
        velocityChangeFactorInTurbo = velocityChangeFactor;
        maxCollisionInTurbo = numberOfCollisions;
        Vector2 currentVelocity = this.getVelocity();
        this.setVelocity(currentVelocity.mult(velocityChangeFactorInTurbo));
        this.renderer().setRenderable(turboBallRenderable);
        this.SetTurboState(true);
        this.countToMaxCollisions();
    }


}
