package io.github.tacascer

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.intellij.lang.annotations.Language
import kotlin.io.path.createFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

class XmlProcessorPluginTest : FunSpec({
    test("given a flatten configuration in extension, when apply plugin, then file should be flattened") {
        val testDir = tempdir()
        val buildFile = testDir.toPath().resolve("build.gradle.kts").createFile()
        val includingFile = testDir.toPath().resolve("sample.xsd").createFile()
        val includedFile = testDir.toPath().resolve("sample_1.xsd").createFile()
        val testOutputFile = testDir.toPath().resolve("sample_flatten.xsd")
        @Language("XML") val includingFileText = """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
                <xs:include schemaLocation="${includedFile.toUri()}"/>
                <xs:element name="sample" type="xs:string"/>
            </xs:schema>
            """.trimIndent()
        includingFile.writeText(includingFileText)
        @Language("XML") val includedFileText = """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
                <xs:element name="sampleOne" type="xs:string"/>
            </xs:schema>
            """.trimIndent()
        includedFile.writeText(includedFileText)
        @Language("XML") val expectedText = """
        <?xml version="1.0" encoding="UTF-8"?>
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
            <xs:element name="sample" type="xs:string" />
            <xs:element name="sampleOne" type="xs:string" />
        </xs:schema>
        """.trimIndent()

        @Language("kotlin")
        val buildFileText = """
            plugins {
                id("io.github.tacascer.xml-processor")
            }
            
            xmlProcessor {
                input.add(layout.buildDirectory.file("${includingFile.toUri()}"))
                output.set(layout.buildDirectory.dir(("${testDir.toURI()}")))
                flatten = true
            } 
        """.trimIndent()
        buildFile.writeText(buildFileText)

        val result =
            GradleRunner.create().withProjectDir(testDir).withArguments("xmlFlatten").withPluginClasspath().build()

        result.task(":xmlFlatten")?.outcome shouldBe TaskOutcome.SUCCESS
        testOutputFile.readText().trimIndent() shouldBe expectedText
    }
})
