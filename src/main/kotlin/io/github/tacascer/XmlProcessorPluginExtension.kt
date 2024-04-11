package io.github.tacascer

import io.github.tacascer.flatten.FlattenParams
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

abstract class XmlProcessorPluginExtension {
    @get:Nested
    abstract val flatten: FlattenParams

    fun flatten(action: Action<in FlattenParams>) {
        action.execute(flatten)
    }
}