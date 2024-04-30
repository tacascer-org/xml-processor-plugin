package io.github.tacascer

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory

interface XmlProcessorPluginExtension {
    @get:InputFiles
    val input: ListProperty<RegularFile>

    @get:OutputDirectory
    val output: DirectoryProperty

    @get:Input
    val flatten: Property<Boolean>
}
