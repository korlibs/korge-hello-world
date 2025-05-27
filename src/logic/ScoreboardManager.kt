package logic

/**
 * Singleton object that manages the game's high score list.
 * Stores player names and scores, and keeps only the top 5 scores.
 */
object ScoreboardManager {
    // Internal list of (name, score) pairs
    private val scores = mutableListOf<Pair<String, Int>>()

    /**
     * Adds a new score to the scoreboard.
     * The list is then sorted in descending order, and only the top 5 scores are retained.
     *
     * @param name The name of the player.
     * @param score The score achieved by the player.
     */
    fun addScore(name: String, score: Int) {
        scores.add(name to score)

        // Sort scores from highest to lowest and keep only the top 5
        val sorted = scores.sortedByDescending { it.second }.take(5)
        scores.clear()
        scores.addAll(sorted)
    }

    /**
     * Returns a list of the top 5 scores (or fewer if less available).
     *
     * @return A list of name-score pairs.
     */
    fun getTopScores(): List<Pair<String, Int>> = scores.toList()
}
