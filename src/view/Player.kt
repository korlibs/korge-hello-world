package view

import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.korge.view.*
import korlibs.time.*

/**
 * Data class that holds both the animated player sprite and its hitbox.
 *
 * @property sprite The visual animated player.
 * @property hitbox A transparent solid rectangle used for collision detection.
 */
data class PlayerWithHitbox(val sprite: Sprite, val hitbox: SolidRect)

/**
 * Creates the animated walking bear sprite and a corresponding hitbox for collision.
 *
 * Loads a sprite sheet with 4 frames, slices it into frames, animates it,
 * and positions it at a fixed X position and provided Y base (usually the ground).
 *
 * @param stage The game stage where the player will be added.
 * @param baseY The Y-coordinate at which the player's feet should touch the ground.
 * @return A PlayerWithHitbox object containing both the animated sprite and its hitbox.
 */
suspend fun createWalkingPlayer(stage: Stage, baseY: Double): PlayerWithHitbox {
    // Load sprite sheet image
    val spriteSheet = resourcesVfs["bear-walking.png"].readBitmap()

    val frameCount = 4
    val frameWidth = spriteSheet.width / frameCount
    val frameHeight = spriteSheet.height

    // Slice the sprite sheet into individual frames
    val frames = (0 until frameCount).map { index ->
        spriteSheet.sliceWithSize(index * frameWidth, 0, frameWidth, frameHeight)
    }

    // Create the walking animation from frames
    val animation = SpriteAnimation(frames, 0.12.seconds)

    // Add the animated sprite to the stage
    val sprite = stage.sprite(animation) {
        anchor(.5, 1.0)            // Anchor at center bottom
        scale = 0.1                // Scale down to fit game design
        position(100.0, baseY)     // Place it on the left side of the screen
        playAnimationLooped()
    }

    // Add a transparent hitbox for collision detection
    val hitbox = stage.solidRect(frameWidth * 0.1, frameHeight * 0.1, Colors.TRANSPARENT) {
        anchor(.5, 1.0)
        position(sprite.x, sprite.y)
    }

    return PlayerWithHitbox(sprite, hitbox)
}
