package io.github.tacascer

import io.github.tacascer.flatten.XMLFlattenTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class XmlProcessorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("xmlProcessor", XmlProcessorPluginExtension::class.java)
        project.tasks.register("xmlFlatten", XMLFlattenTask::class.java) {
            it.apply {
                group = "xml"
                description = "Flatten XML file"
                inputFile.set(extension.flatten.inputFile)
                outputFile.set(extension.flatten.outputFile)
            }
        }
    }
}
