package io.github.tacascer.flatten

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface FlattenParams {
    val inputFile: RegularFileProperty
    val outputFile: RegularFileProperty
    val stripNamespaces: Property<Boolean>
}
