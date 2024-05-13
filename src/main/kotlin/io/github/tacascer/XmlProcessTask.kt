package io.github.tacascer

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * A Gradle task that processes XML files using a list of filters.
 */
abstract class XmlProcessTask : DefaultTask() {
    /**
     * A list of input files to be processed. These files should be XML files.
     */
    @get:InputFiles
    abstract val inputFiles: ListProperty<RegularFile>

    /**
     * The output directory where the processed files will be saved.
     */
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    /**
     * A list of filters to apply during the processing of the XML files.
     * Each filter defines a specific transformation or operation to perform on the XML data.
     */
    @get:Input
    abstract val filters: ListProperty<XmlFilter>

    @TaskAction
    fun process() {
        inputFiles.get().forEach {
            logger.debug("Processing {}", it.asFile.name)
            val outputFile = outputDir.file(it.asFile.name).get().asFile.toPath()
            XmlFilterChain(filters.get()).process(it.asFile.toPath(), outputFile)
        }
    }
}
