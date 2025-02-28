package bricker.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.collisions.GameObjectCollection;

/**
 * Represents a falling heart in the Bricker game.
 * The heart increases the player's health when it collides with the main paddle,
 * provided the player's health is below the maximum limit.
 */
public class FallingHeart extends Heart {
    private final GameObject mainPaddle;
    private final GameObjectCollection gameObjects;
    private final HealthBar healthBar;
    private static final int MAX_LIVES = 4;



    /**
     * Constructs a FallingHeart instance.
     *
     * @param topLeftCorner Position of the heart in window coordinates (pixels).
     * @param dimensions    Width and height of the heart in window coordinates.
     * @param renderable    The renderable representing the heart.
     * @param mainPaddle    The main paddle that the heart interacts with.
     * @param gameObjects   The collection of game objects in the game.
     * @param healthBar     The health bar to be updated when the heart is collected.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        GameObject mainPaddle, Renderable heartImage, GameObjectCollection gameObjects, HealthBar healthBar ) {
        super(topLeftCorner, dimensions, renderable);
        this.mainPaddle = mainPaddle;
        this.gameObjects = gameObjects;
        this.healthBar = healthBar;

    }


    /**
     * Handles collision behavior for the falling heart.
     * Adds a life to the health bar if the heart collides with the main paddle
     * and the player's health is below the maximum limit. The heart is removed
     * from the game after the collision.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Collision details, including the normal of the collision.
     */
    @Override
        public void onCollisionEnter(GameObject other, Collision collision) {
            super.onCollisionEnter(other, collision);
            if(!shouldCollideWith(other)){
                return;
            }
            this.healthBar.addLife();
            gameObjects.removeGameObject(this);
    }


    /**
     * Determines if the heart should collide with the given object.
     * Only allows collisions with the main paddle if the player's health
     * is below the maximum limit.
     *
     * @param other The other GameObject to check for collision.
     * @return True if the heart should collide with the main paddle; false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        super.shouldCollideWith(other);
        if(other == mainPaddle && healthBar.getLives() < MAX_LIVES){
            return true;
        }

        return false;

    }


}
