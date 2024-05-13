package io.github.tacascer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class XmlProcessorPluginTest : FunSpec({
    lateinit var project: Project
    lateinit var plugin: XmlProcessorPlugin

    beforeTest {
        project = ProjectBuilder.builder().build()
        plugin = XmlProcessorPlugin()
    }
    test("XmlProcessorPlugin should register a xmlProcessor task") {
        plugin.apply(project)

        val extension = project.extensions.getByType(XmlProcessorPluginExtension::class.java)

        extension.processingSets.create("first") {
            it.inputFiles.add(project.layout.projectDirectory.file("input.xml"))
        }

        project.tasks.getByName("processXmlSetFirst").shouldBeInstanceOf<XmlProcessTask>()
    }

    test("plugin should register a xmlProcessor task for each set") {
        plugin.apply(project)

        val extension = project.extensions.getByType(XmlProcessorPluginExtension::class.java)

        extension.processingSets.create("first") {
            it.inputFiles.add(project.layout.projectDirectory.file("input.xml"))
        }

        extension.processingSets.create("second") {
            it.inputFiles.add(project.layout.projectDirectory.file("input.xml"))
        }

        project.tasks.getByName("processXmlSetFirst").shouldBeInstanceOf<XmlProcessTask>()
        project.tasks.getByName("processXmlSetSecond").shouldBeInstanceOf<XmlProcessTask>()
    }
})
