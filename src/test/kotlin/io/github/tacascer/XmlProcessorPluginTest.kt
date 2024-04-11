package io.github.tacascer

import io.github.tacascer.flatten.XMLFlattenTask
import io.kotest.core.spec.style.FunSpec
import org.gradle.testfixtures.ProjectBuilder

class XmlProcessorPluginTest : FunSpec({
    test("XmlProcessorPlugin should register xmlFlatten task") {
        val project = ProjectBuilder.builder().build()
        val plugin = XmlProcessorPlugin()

        plugin.apply(project)

        project.tasks.getByName("xmlFlatten") is XMLFlattenTask
    }
})
