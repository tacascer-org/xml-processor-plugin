import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `jvm-test-suite`
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.sonarqube") version "5.0.0.4638"
    kotlin("jvm") version "1.9.23"
    signing
}

group = "io.github.tacascer"
version = "0.0.1"

repositories {
    mavenCentral()
}

val kotestVersion = "5.8.1"
val jetbrainsAnnotationVersion = "24.1.0"
val slf4jSimpleVersion = "2.0.12"
val kotlinLoggingVersion = "6.0.4"
val xmlProcessorVersion = "0.2.1"

dependencies {
    compileOnly("org.jetbrains:annotations:$jetbrainsAnnotationVersion")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("io.github.tacascer:xml-processor:$xmlProcessorVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.slf4j:slf4j-simple:$slf4jSimpleVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
    jvmToolchain(17)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("functionalTest") {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

gradlePlugin {
    website = "https://github.com/tacascer-org/xml-processor-plugin"
    vcsUrl = "https://github.com/tacascer-org/xml-processor-plugin"
    plugins {
        create("xml-processor") {
            id = "io.github.tacascer.xml-processor"
            displayName = "Xml Processor"
            description = "A plugin to flatten XML files."
            tags = listOf("xml", "flatten", "processor")
            implementationClass = "io.github.tacascer.XmlProcessor"
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

tasks.sonar {
    dependsOn(tasks.named("koverXmlReport"))
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "tacascer-org_xml-processor-plugin")
        property("sonar.organization", "tacascer-org")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.asFile.get()}/reports/kover/report.xml"
        )
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}
