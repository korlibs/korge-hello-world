import korlibs.korge.view.*

/**
 * Interface for providing game stage-related UI behavior.
 * Implemented by game logic classes to expose UI control functions
 * to external components (e.g. UI buttons, modals).
 */
interface GameLogicStageProvider {

    /**
     * Displays the retry button on the game screen,
     * typically after game over and name input.
     */
    fun showRetryButton()
}
