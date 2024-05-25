package io.github.tacascer

import io.github.tacascer.flatten.IncludeFlattener
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.paths.shouldContainFile
import org.gradle.testfixtures.ProjectBuilder
import org.intellij.lang.annotations.Language
import kotlin.io.path.createFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

private const val INPUT_FILE = "sample.xsd"

class XmlProcessTaskTest : FunSpec({
    test("task can flatten file") {
        val testDir = tempdir().toPath()
        val includingFile = testDir.resolve(INPUT_FILE).createFile()
        val includedFile = testDir.resolve("sample_1.xsd").createFile()
        val testOutputDir = tempdir().resolve("output").toPath()

        @Language("XML")
        val includingFileText =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
                <xs:include schemaLocation="${includedFile.toUri()}"/>
                <xs:element name="sample" type="xs:string"/>
            </xs:schema>
            """.trimIndent()
        includingFile.writeText(includingFileText)
        @Language("XML")
        val includedFileText =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
                <xs:element name="sampleOne" type="xs:string"/>
            </xs:schema>
            """.trimIndent()
        includedFile.writeText(includedFileText)

        @Language("XML")
        val expectedText =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://www.sample.com">
                <xs:element name="sample" type="xs:string" />
                <xs:element name="sampleOne" type="xs:string" />
            </xs:schema>
            
            """.trimIndent()
        val project = ProjectBuilder.builder().build()
        val xmlProcess =
            project.tasks.register("xmlProcess", XmlProcessTask::class.java) {
                it.apply {
                    inputFiles.add(project.layout.projectDirectory.file(includingFile.toUri().toString()))
                    outputDir.set(project.layout.projectDirectory.dir(testOutputDir.toUri().toString()))
                    filters.add(IncludeFlattener())
                }
            }

        xmlProcess.get().process()

        testOutputDir shouldContainFile INPUT_FILE
        testOutputDir.resolve(INPUT_FILE).readText() shouldBeSamePrettyPrintedAs expectedText
    }
})
