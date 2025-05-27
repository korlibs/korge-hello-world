<<<<<<< HEAD
import korlibs.time.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
	override suspend fun SContainer.sceneMain() {
		val minDegrees = (-16).degrees
		val maxDegrees = (+16).degrees

		val image = image(resourcesVfs["korge.png"].readBitmap()) {
			rotation = maxDegrees
			anchor(.5, .5)
			scale(0.8)
			position(256, 256)
		}

		while (true) {
			image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
			image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
		}
	}
=======
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.math.geom.*
import logic.*

/**
 * Entry point for the game.
 * Initializes the KorGE engine with a specific window size, background color, and title.
 * Then creates and starts the main game logic.
 */
suspend fun main() = Korge(
    windowSize = Size(640, 360),          // Set window size to 640x360 pixels
    bgcolor = Colors["#cffae5"],          // Set background color to a soft green/blue
    title = "Bear on the road"            // Title displayed in the window bar
) {
    // Create a new instance of the game logic and start the game
    val game = GameLogic(this)
    game.start()
>>>>>>> d9275b4 (Added the game)
}
