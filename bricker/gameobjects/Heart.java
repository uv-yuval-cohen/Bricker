package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a heart object in the Bricker game.
 * Hearts are used in the game for visual representation, such as in the health bar.
 */
public class Heart extends GameObject {

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

}
