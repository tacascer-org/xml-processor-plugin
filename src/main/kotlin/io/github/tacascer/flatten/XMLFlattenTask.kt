package io.github.tacascer.flatten

import io.github.oshai.kotlinlogging.KotlinLogging
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import kotlin.io.path.deleteIfExists

val log = KotlinLogging.logger {}

abstract class XMLFlattenTask : DefaultTask() {
    @get:InputFiles
    abstract val inputFiles: ListProperty<RegularFile>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun flatten() {
        val flattener = IncludeFlattener()
        inputFiles.get().map {
            val outputFile =
                outputDir.file("${it.asFile.nameWithoutExtension}_flatten.${it.asFile.extension}")
                    .get().asFile
            outputFile.toPath().deleteIfExists()
            outputFile.createNewFile()
            log.info { "Flattening file: ${it.toPath()} into ${outputFile.toPath()}" }
            flattener.process(
                it.toPath(),
                outputFile.toPath(),
            )
        }
    }

    private fun RegularFile.toPath(): Path = asFile.toPath()
}
