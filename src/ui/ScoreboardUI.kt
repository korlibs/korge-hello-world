package ui

import GameLogicStageProvider
import korlibs.korge.view.*
import korlibs.korge.ui.*
import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.align.*
import logic.ScoreboardManager

/**
 * Displays a name entry UI after the game ends,
 * allowing the player to input their name and save their score.
 *
 * @param score The final score to be saved with the player's name.
 * @param onSaved Callback triggered after the name is saved and the UI is cleaned up.
 */
fun Container.showNameEntry(score: Int, onSaved: () -> Unit) {
    val screenHeight = stage?.height ?: 600.0

    // Label for the name input
    val label = text("Name:", textSize = 24.0, color = Colors.BLACK) {
        centerXOnStage()
        y = screenHeight / 2 - 100
    }

    // Input field for player's name
    val input = uiTextInput {
        size(160.0, 36.0)
        textSize = 20.0
        centerXOnStage()
        y = screenHeight / 2 - 50
    }

    // Save button to confirm entry and store the score
    val saveButton = text("✔️ Save", textSize = 24.0, color = Colors.GREEN) {
        centerXOnStage()
        y = screenHeight / 2
    }

    // Handle save action
    saveButton.onClick {
        if (input.text.isNotBlank()) {
            // Add the score to the scoreboard
            ScoreboardManager.addScore(input.text.trim(), score)

            // Clean up UI elements
            label.removeFromParent()
            input.removeFromParent()
            saveButton.removeFromParent()

            // Show top scores and invoke callback
            stage?.showTopScores(ScoreboardManager.getTopScores())
            onSaved()
        }
    }
}

/**
 * Displays the top 5 high scores on the screen.
 * Removes any previous score labels before showing the latest ones.
 *
 * @param topScores A list of (player name, score) pairs to display.
 */
fun Container.showTopScores(topScores: List<Pair<String, Int>>) {
    val startX = (stage?.width ?: 800.0) - 200.0
    val startY = 20.0

    // Remove any existing score labels
    this.children.filter { it.name?.startsWith("scoreLabel_") == true }.forEach {
        it.removeFromParent()
    }

    // Add each score as a new label to the screen
    topScores.take(5).forEachIndexed { index, (playerName, score) ->
        text("${index + 1}. $playerName: $score", textSize = 18.0, color = Colors.BLACK) {
            position(startX, startY + index * 24)
            this.name = "scoreLabel_$index"
        }
    }
}
