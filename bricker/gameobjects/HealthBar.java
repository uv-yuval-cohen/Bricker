package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Represents a health bar in the Bricker game.
 * Displays the player's remaining lives as a series of heart icons in the UI layer.
 */
public class HealthBar {
    private static final int INITIAL_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private final int FIRST_LIFE_POSITION = 12;
    private final int SPACE_BETWEEN_LIVES = 30;
    private final int HEALTH_BAR_HEIGHT = 15;
    private final int HEART_RADIUS = 20;

    private int lives = INITIAL_LIVES;
    private Heart[] heartsToDisplay = new Heart[MAX_LIVES];
    private final Renderable heartImage;
    private final int heartRadius;
    private final GameObjectCollection gameObjects;


    /**
     * Constructs a HealthBar instance.
     *
     * @param heartImage   The renderable image representing a single heart.
     * @param heartRadius  The radius of each heart icon in the health bar.
     * @param gameObjects  The collection of game objects to manage hearts in the health bar.
     */
    public HealthBar(Renderable heartImage, int heartRadius, GameObjectCollection gameObjects) {
        this.heartImage = heartImage;
        this.heartRadius = heartRadius;
        this.gameObjects = gameObjects;
        this.initHearts();
    }


    /**
     * Gets the current number of lives.
     *
     * @return The number of lives remaining.
     */
    public int getLives() {

        return this.lives;
    }

    /**
     * Adds a life to the health bar, up to the maximum allowed lives.
     * A new heart icon is displayed in the UI for each additional life.
     */
    public void addLife() {
        if (lives < MAX_LIVES) {
            this.lives += 1;
            heartsToDisplay[lives-1] = new Heart(
                    new Vector2(FIRST_LIFE_POSITION +  (lives-1)* SPACE_BETWEEN_LIVES, HEALTH_BAR_HEIGHT), // Position each heart with 30px spacing
                    new Vector2(heartRadius, heartRadius), // Size of each heart
                    heartImage
            );
            gameObjects.addGameObject(heartsToDisplay[lives-1], Layer.UI);
        }
    }

    /**
     * Removes a life from the health bar, down to a minimum of zero.
     * The corresponding heart icon is removed from the UI.
     */
    public void removeLife() {
        if (this.lives > 0) {
            Heart heartToRemove = this.heartsToDisplay[this.lives-1];
            this.heartsToDisplay[this.lives-1] = null;
            this.gameObjects.removeGameObject(heartToRemove, Layer.UI);
            this.lives -= 1;
        }
    }

    /**
     * Initializes the health bar with the initial number of hearts.
     * Adds heart icons to the UI for the starting number of lives.
     */
    private void initHearts() {
        for (int i = 0; i < INITIAL_LIVES; i++) {
            Heart heart = new Heart(
                    new Vector2(FIRST_LIFE_POSITION + i * SPACE_BETWEEN_LIVES, HEALTH_BAR_HEIGHT), // Position each heart with 30px spacing
                    new Vector2(HEART_RADIUS, HEART_RADIUS), // Size of each heart
                    heartImage
            );
            this.heartsToDisplay[i] = heart; // Store the heart in the array
            gameObjects.addGameObject(heart, Layer.UI); // Add heart to UI layer
        }
    }

}

