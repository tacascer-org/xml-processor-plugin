package io.github.tacascer.flatten

import org.gradle.api.file.RegularFileProperty

interface FlattenParams {
    val inputFile: RegularFileProperty
    val outputFile: RegularFileProperty
}