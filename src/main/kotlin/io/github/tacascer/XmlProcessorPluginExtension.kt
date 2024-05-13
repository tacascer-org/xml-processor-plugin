package io.github.tacascer

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty

/**
 * This interface represents the XML Processor Plugin extension.
 * It contains a collection of processing sets that define how XML files should be processed.
 */
interface XmlProcessorPluginExtension {
    /**
     * A collection of processing sets. Each processing set defines a unique way of processing XML files.
     */
    val processingSets: NamedDomainObjectContainer<ProcessingSet>
}

/**
 * This interface represents a set of processing instructions for XML files.
 * It includes the input files to be processed, the output directory, and a list of filters to apply during processing.
 */
interface ProcessingSet {
    /**
     * The name of the processing set. This is used to uniquely identify the processing set.
     */
    val name: String

    /**
     * A list of input files to be processed. These files should be XML files.
     */
    val inputFiles: ListProperty<RegularFile>

    /**
     * The output directory where the processed files will be saved.
     * Defaults to $buildDir/xml/[name]
     */
    val outputDir: DirectoryProperty

    /**
     * A list of filters to apply during the processing of the XML files.
     * Each filter defines a specific transformation or operation to perform on the XML data.
     */
    val filters: ListProperty<XmlFilter>
}
