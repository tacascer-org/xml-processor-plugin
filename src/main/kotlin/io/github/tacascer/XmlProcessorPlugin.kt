package io.github.tacascer

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

class XmlProcessorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("xmlProcessor", XmlProcessorPluginExtension::class.java)
        extension.processingSets.all { processingSet ->
            processingSet.setConventions(project)
            project.registerProcessingTasks(processingSet)
        }
    }
}

private fun ProcessingSet.setConventions(project: Project) {
    outputDir.convention(project.layout.buildDirectory.dir("xml/$name"))
}

private fun Project.registerProcessingTasks(processingSet: ProcessingSet) {
    tasks.register("processXmlSet${processingSet.name.capitalized()}", XmlProcessTask::class.java) {
        it.apply {
            group = "xmlProcessor"
            description = "Process XML files"
            inputFiles.set(processingSet.inputFiles)
            outputDir.set(processingSet.outputDir)
            filters.set(processingSet.filters)
        }
    }
}
