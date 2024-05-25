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
    test("given an empty list of filters, when the plugin is applied, it should do nothing") {
        val testDir = tempdir()
        val buildFile = testDir.toPath().resolve("build.gradle.kts").createFile()
        val inputFile = testDir.toPath().resolve("sample.xsd").createFile()
        val testOutputFile = testDir.toPath().resolve("output/sample.xsd")

        @Language("XML")
        val inputFileText =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
                <xs:element name="sample" type="xs:string"/>
            </xs:schema>
            """.trimIndent()
        inputFile.writeText(inputFileText)
        @Language("XMl")
        val expectedText =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
                <xs:element name="sample" type="xs:string" />
            </xs:schema>
            """.trimIndent()

        @Language("kotlin")
        val buildFileText =
            """
            plugins {
                id("io.github.tacascer.xml-processor")
            }
            
            xmlProcessor {
                processingSets {
                    register("include") {
                        inputFiles.add(layout.buildDirectory.file("${inputFile.toUri()}"))
                        outputDir.set(layout.buildDirectory.dir(("${testDir.resolve("output").toURI()}")))
                    }
                }
            } 
            """.trimIndent()
        buildFile.writeText(buildFileText)

        val result =
            GradleRunner.create().withProjectDir(testDir).withArguments("processXmlSetInclude").withPluginClasspath()
                .withDebug(true).forwardOutput()
                .build()

        result.task(":processXmlSetInclude")?.outcome shouldBe TaskOutcome.SUCCESS
        testOutputFile.readText().trimIndent() shouldBe expectedText
    }
})
