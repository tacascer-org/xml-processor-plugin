package io.github.tacascer.flatten

import io.github.tacascer.XmlIncludeFlattener
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class XMLFlattenTask : DefaultTask() {
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun flatten() {
        val file = outputFile.get().asFile
        val flattener = XmlIncludeFlattener(inputFile.get().asFile.toPath())
        file.parentFile.mkdirs()
        flattener.process(file.toPath())
    }
}