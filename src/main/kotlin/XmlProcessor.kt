package io.github.tacascer

import org.gradle.api.Plugin
import org.gradle.api.Project

class XmlProcessor : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("xmlProcessor")
    }
}
