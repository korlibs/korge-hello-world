package org.korge.application

import korlibs.render.GameWindowCreationConfig
import korlibs.render.KorgwActivity
import main

class MainActivity : KorgwActivity(config = GameWindowCreationConfig(msaa = 1)) {
    override suspend fun activityMain() {
        main()
    }
}
