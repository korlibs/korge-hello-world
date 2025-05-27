package view

import korlibs.korge.view.*
import korlibs.image.color.*

/**
 * Creates and places the ground as a solid rectangle at the bottom of the screen.
 *
 * The ground is a black bar that spans the full width of the stage and is placed
 * flush with the bottom edge. It acts as the physical "floor" in the game world.
 *
 * @param stage The game stage where the ground will be drawn.
 * @param height The height of the ground (default is 40.0).
 * @return The SolidRect object representing the ground.
 */
fun createGround(stage: Stage, height: Double = 40.0): SolidRect {
    val ground = stage.solidRect(stage.width, height, Colors["#000000"])
    ground.y = stage.height - height
    return ground
}
