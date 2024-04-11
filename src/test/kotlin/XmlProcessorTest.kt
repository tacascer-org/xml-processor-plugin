import io.github.tacascer.XmlProcessor
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import org.gradle.testfixtures.ProjectBuilder

class XmlProcessorTest : FunSpec({
    test("XmlProcessorPlugin should register xmlProcessor task") {
        val project = ProjectBuilder.builder().build()
        val plugin = XmlProcessor()

        plugin.apply(project)

        project.tasks.getByName("xmlProcessor") shouldNotBe null
    }
}
)
