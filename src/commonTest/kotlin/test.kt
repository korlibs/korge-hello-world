import com.soywiz.klock.*
import com.soywiz.korge.input.*
import com.soywiz.korge.tests.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import kotlin.test.*

class MyTest : ViewsForTesting() {
    @Test
    fun test() = viewsTest {
        views.stage.apply {
            val log = arrayListOf<String>()
            val rect = solidRect(100, 100, Colors.RED)
            rect.onClick {
                log += "clicked"
            }
            val start = time
            assertEquals(1, views.stage.children.size)
            rect.simulateClick()
            assertEquals(true, rect.isVisibleToUser())
            rect.tween(rect::x[-102], time = 10.seconds)
            println(rect.globalBounds)
            val end = time
            println(end - start)
            assertEquals(false, rect.isVisibleToUser())
            assertEquals(listOf("clicked"), log)
        }
    }
}