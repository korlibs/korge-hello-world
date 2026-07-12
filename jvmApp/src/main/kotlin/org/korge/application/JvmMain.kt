package org.korge.application

import korlibs.io.async.runBlockingNoJs
import main

object JvmMain {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlockingNoJs {
        main()
    }
}
