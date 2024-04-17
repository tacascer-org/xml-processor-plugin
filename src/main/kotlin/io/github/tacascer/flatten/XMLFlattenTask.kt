package io.github.tacascer.flatten

import io.github.tacascer.XmlIncludeFlattener
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class XMLFlattenTask : DefaultTask() {
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val stripNamespaces: Property<Boolean>

    @TaskAction
    fun flatten() {
        val file = outputFile.get().asFile
        val flattener = XmlIncludeFlattener(inputFile.get().asFile.toPath(), stripNamespace = stripNamespaces.get())
        file.parentFile.mkdirs()
        flattener.process(file.toPath())
    }
}
