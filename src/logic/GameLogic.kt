package logic

import GameLogicStageProvider
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.time.*
import kotlinx.coroutines.*
import ui.*
import view.*
import kotlin.random.*
import logic.spawnObstacle
import kotlin.time.*

// Main class that handles game logic such as physics, score, input and obstacle spawning
class GameLogic(val stage: Stage) : GameLogicStageProvider {

    // Game state variables
    private var isJumping = false
    private var landedAfterJump = false
    private var gameStarted = false
    private var gameOver = false
    private var velocityY = 0.0
    private var score = 0

    // Player components
    private lateinit var player: Sprite
    private lateinit var playerHitbox: SolidRect

    // UI elements
    private lateinit var scoreText: Text
    private lateinit var playButton: Text
    private lateinit var retryButton: Text
    private lateinit var ground: SolidRect

    // Physics constants
    private val gravity = 1600
    private val jumpForce = -600
    private val groundHeight = 40.0

    // Obstacle management
    private val obstacles = arrayListOf<View>()
    private val obstacleHitboxes = arrayListOf<SolidRect>()
    private var obstacleSpeed = 200.0

    private var timeSinceLastSpawn = 0.seconds
    private var nextSpawnDelay = Random.nextDouble(1.0, 2.5).seconds

    // Initializes game scene
    suspend fun start() {
        ground = createGround(stage, groundHeight)
        val baseY = ground.y + ground.height

        val playerData = createWalkingPlayer(stage, baseY)
        player = playerData.sprite
        playerHitbox = playerData.hitbox

        scoreText = createScoreText(stage)
        val buttons = createButtons(stage, this)
        playButton = buttons.first
        retryButton = buttons.second
        retryButton.name = "retry"

        playButton.onClick { startGame() }
        retryButton.onClick { startGame() }

        playButton.visible = true
        retryButton.visible = false

        handleClick()
        handleUpdate()
    }

    // Handles screen tap input for jumping
    private fun handleClick() {
        stage.onClick {
            if (!gameOver && !isJumping && gameStarted) {
                velocityY = jumpForce.toDouble()
                isJumping = true
                landedAfterJump = true
            }
        }
    }

    // Handles the game update loop (called every frame)
    private fun handleUpdate() {
        stage.addUpdater { dt ->
            if (!gameStarted || gameOver) return@addUpdater

            // Update player physics
            val baseY = ground.y + ground.height
            val result = updatePlayerPhysics(player, baseY, dt, gravity, velocityY, isJumping)
            velocityY = result.first

            if (result.second) {
                isJumping = false
                velocityY = 0.0
                if (landedAfterJump) {
                    // Increase score over time
                    score += (100 * dt.seconds).toInt()
                    scoreText.text = "Score: $score"
                    landedAfterJump = false

                    // Increase difficulty
                    if (score % 10 == 0) {
                        obstacleSpeed += 50
                    }
                }
            }

            // Move obstacles and sync hitboxes
            obstacles.forEachIndexed { index, obstacle ->
                obstacle.x -= obstacleSpeed * dt.seconds
                obstacleHitboxes[index].position(obstacle.x, obstacle.y)
            }

            // Remove obstacles that moved off-screen
            val toRemove = obstacles.withIndex().filter { it.value.x < -it.value.width }.map { it.index }
            toRemove.sortedDescending().forEach {
                obstacles[it].removeFromParent()
                obstacleHitboxes[it].removeFromParent()
                obstacles.removeAt(it)
                obstacleHitboxes.removeAt(it)
            }

            // Spawn new obstacles at intervals
            timeSinceLastSpawn += dt
            if (timeSinceLastSpawn >= nextSpawnDelay) {
                launch {
                    spawnObstacle(stage, ground.y + ground.height, obstacles, obstacleHitboxes)
                }
                timeSinceLastSpawn = 0.seconds

                // Dynamic spawn delay scaling with speed
                val speedFactor = 200.0 / obstacleSpeed
                nextSpawnDelay = Random.nextDouble(1.0, 2.5) * speedFactor.seconds
            }

            // Update player hitbox position
            playerHitbox.position(player.x, player.y)

            // Check for collision between player and obstacles
            obstacleHitboxes.firstOrNull {
                playerHitbox.globalBounds.intersects(it.globalBounds)
            }?.let {
                gameOver = true
                gameStarted = false
                scoreText.text = "Game Over! Final score: $score"
                playButton.visible = false
                retryButton.visible = false

                // Show name entry modal after game over
                stage.showNameEntry(score) {
                    showRetryButton()
                }
            }
        }
    }

    // Resets and starts the game
    fun startGame() {
        gameStarted = true
        gameOver = false
        isJumping = false
        landedAfterJump = false
        velocityY = 0.0
        score = 0
        scoreText.text = "Score: $score"
        obstacleSpeed = 200.0

        // Remove all UI elements related to previous session (except retry)
        stage.children.filter {
            (it is Text || it is Container) && it.name != "retry"
        }.filter {
            (it as? Text)?.text?.contains(" - ") == true ||
                (it as? Text)?.text?.contains("Name:") == true ||
                (it as? Text)?.text == "✔️ Save"
        }.forEach { it.removeFromParent() }

        // Clear all obstacles and hitboxes
        obstacles.clear()
        obstacleHitboxes.forEach { it.removeFromParent() }
        obstacleHitboxes.clear()

        // Remove all obstacle views from stage
        stage.stage.children.filter { it.name == "obstacle" }.forEach { it.removeFromParent() }

        // Reset player position
        val baseY = ground.y + ground.height
        player.position(100.0, baseY)

        // Hide buttons
        retryButton.visible = false
        playButton.visible = false
    }

    // Shows the retry button after game over and name input
    override fun showRetryButton() {
        retryButton.visible = true
    }
}
