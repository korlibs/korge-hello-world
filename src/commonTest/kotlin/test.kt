import com.soywiz.korge.tests.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import kotlin.test.*

class MyTest : ViewsForTesting() {
    @Test
    fun test() = viewsTest {
        views.stage.apply {
            val rect = solidRect(100, 100, Colors.RED)
            assertEquals(1, views.stage.children.size)
        }
    }
}