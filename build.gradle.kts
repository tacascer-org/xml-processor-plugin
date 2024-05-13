import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `jvm-test-suite`
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.sonarqube") version "5.0.0.4638"
    kotlin("jvm") version "1.9.24"
    signing
}

group = "io.github.tacascer"
version = "0.2.1" // x-release-please-version

repositories {
    mavenCentral()
}

val kotestVersion = "5.9.0"
val jetbrainsAnnotationVersion = "24.1.0"
val slf4jSimpleVersion = "2.0.13"
val kotlinLoggingVersion = "6.0.9"
val xmlProcessorVersion = "0.6.0"

dependencies {
    compileOnly("org.jetbrains:annotations:$jetbrainsAnnotationVersion")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("io.github.tacascer:xml-processor:$xmlProcessorVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
    jvmToolchain(17)
}

testing {
    suites {
        withType<JvmTestSuite> {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
                runtimeOnly("org.slf4j:slf4j-simple:$slf4jSimpleVersion")
            }
        }

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val functionalTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
                implementation(gradleApi())
                implementation(gradleTestKit())
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

tasks.check {
    dependsOn(testing.suites.named("functionalTest"))
}

gradlePlugin {
    website = "https://github.com/tacascer-org/xml-processor-plugin"
    vcsUrl = "https://github.com/tacascer-org/xml-processor-plugin"
    plugins {
        create("xml-processor") {
            description = "A plugin to flatten XML files."
            displayName = "Xml Processor"
            id = "io.github.tacascer.xml-processor"
            implementationClass = "io.github.tacascer.XmlProcessorPlugin"
            tags = listOf("xml", "flatten", "processor")
        }
    }
    testSourceSets(sourceSets.getByName("test"), sourceSets.getByName("functionalTest"))
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
