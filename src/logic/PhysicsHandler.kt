package logic

import korlibs.korge.view.*
import korlibs.time.*

/**
 * Updates the vertical physics of the player character during a jump.
 *
 * Applies gravity, updates the player's vertical position, and determines
 * if the player has landed back on the ground.
 *
 * @param player The player sprite whose position is updated.
 * @param groundY The Y-position representing the ground level.
 * @param dt The time delta since the last frame (used for frame-independent physics).
 * @param gravity The gravitational acceleration value applied to the player.
 * @param velocityY The current vertical velocity of the player.
 * @param isJumping A flag indicating whether the player is currently jumping.
 *
 * @return A Pair containing the updated vertical velocity and a boolean indicating whether the player has landed.
 */
fun updatePlayerPhysics(
    player: Sprite,
    groundY: Double,
    dt: TimeSpan,
    gravity: Int,
    velocityY: Double,
    isJumping: Boolean
): Pair<Double, Boolean> {
    var vy = velocityY
    var landed = false

    if (isJumping) {
        // Apply gravity to vertical velocity
        vy += gravity * dt.seconds

        // Update player position based on velocity
        player.y += vy * dt.seconds

        // Check if player has landed on the ground
        if (player.y >= groundY) {
            player.y = groundY
            vy = 0.0
            landed = true
        }
    }

    // Return updated velocity and landing status
    return vy to landed
}
