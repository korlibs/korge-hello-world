package ui

import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import logic.*

/**
 * Creates and returns the main UI buttons used in the game:
 * - A green "PLAY" button shown before the game starts.
 * - A red "RETRY" button shown after game over.
 *
 * Both buttons are centered horizontally. The "PLAY" button is positioned
 * slightly above center, and the "RETRY" button slightly below center.
 *
 * @param stage The container (usually the main game stage) where buttons will be added.
 * @param game The GameLogic instance, used to trigger game actions (not used here but passed for consistency).
 * @return A pair containing the play button and retry button.
 */
fun createButtons(stage: Container, game: GameLogic): Pair<Text, Text> {
    // Create the "PLAY" button and center it slightly above the middle of the screen
    val playButton = stage.text("► PLAY", textSize = 32.0, color = Colors.GREEN) {
        centerXOnStage()
        centerYOnStage()
        y -= 20.0
    }

    // Create the "RETRY" button, initially hidden, slightly below the middle
    val retryButton = stage.text("RETRY", textSize = 32.0, color = Colors.RED) {
        name = "retry"
        centerXOnStage()
        y = stage.height / 2 + 10
        visible = false
    }

    // Return both buttons as a pair
    return Pair(playButton, retryButton)
}
