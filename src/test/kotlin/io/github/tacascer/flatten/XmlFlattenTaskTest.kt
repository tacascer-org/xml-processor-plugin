package io.github.tacascer.flatten

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.paths.shouldContainFile
import io.kotest.matchers.shouldBe
import org.gradle.testfixtures.ProjectBuilder
import org.intellij.lang.annotations.Language
import kotlin.io.path.createFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

class XmlFlattenTaskTest : FunSpec({
    test("XmlFlattenTask should flatten XML file with include directive") {
        val testDir = tempdir().toPath()
        val includingFile = testDir.resolve("sample.xsd").createFile()
        val includedFile = testDir.resolve("sample_1.xsd").createFile()
        val testOutputFile = testDir.resolve("sample_flatten.xsd")
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
        val project = ProjectBuilder.builder().build()
        val xmlFlatten = project.tasks.register("xmlFlatten", XMLFlattenTask::class.java) {
            it.apply {
                inputFiles.add(project.layout.projectDirectory.file(includingFile.toUri().toString()))
                outputDir.set(testDir.toFile())
            }
        }

        xmlFlatten.get().flatten()

        testDir shouldContainFile "sample_flatten.xsd"
        testOutputFile.readText().trimIndent() shouldBe expectedText
    }
})
