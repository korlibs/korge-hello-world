package logic

import korlibs.image.color.Colors
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.view.*

/**
 * Spawns a new obstacle (a car) on the right side of the screen,
 * and adds it along with a corresponding hitbox to the provided lists.
 *
 * @param stage The current game stage where the obstacle will be added.
 * @param baseY The Y-coordinate baseline for placing the obstacle (usually ground level).
 * @param obstacles A list to which the visual obstacle view will be added.
 * @param obstacleHitboxes A list to which the obstacle's hitbox will be added for collision detection.
 */
suspend fun spawnObstacle(
    stage: Stage,
    baseY: Double,
    obstacles: MutableList<View>,
    obstacleHitboxes: MutableList<SolidRect>
) {
    // Load the car image asset
    val carBitmap = resourcesVfs["retro-car.png"].readBitmap()
    val scale = 0.2

    // Create the car image on screen and set its position and anchor
    val car = stage.image(carBitmap) {
        name = "obstacle"
        anchor(.5, 1.0)
        this.scale = scale
        position(stage.width + carBitmap.width * scale / 2, baseY)
    }

    // Create a hitbox for the car (roughly centered and slightly reduced in size for better collision)
    val carHitbox = stage.solidRect(car.width * 0.1, car.height * 0.1, Colors.TRANSPARENT) {
        anchor(.5, 1.0)
        position(car.x, car.y - (car.height * 0.2))
    }

    // Add the obstacle and its hitbox to their respective lists
    obstacles.add(car)
    obstacleHitboxes.add(carHitbox)
}
