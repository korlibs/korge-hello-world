package ui

import korlibs.korge.view.*
import korlibs.image.color.*

/**
 * Creates and displays the score text at the top-left corner of the screen.
 *
 * This function initializes the score label with a default value of 0,
 * and positions it at coordinates (10, 10) within the given container.
 *
 * @param stage The container (usually the game stage) where the score text will be added.
 * @return A Text object that can be updated later to reflect the current score.
 */
fun createScoreText(stage: Container): Text {
    return stage.text("Score: 0", textSize = 24.0, color = Colors.BLACK) {
        position(10, 10)
    }
}
