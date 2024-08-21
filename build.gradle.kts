import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.changelog.date
import org.jetbrains.changelog.Changelog

fun properties(key: String) = project.findProperty(key).toString()
fun environment(key: String) = providers.environmentVariable(key)

//plugins {
//    // Java support
//    id("java")
//    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.7.10"
//    // Gradle IntelliJ Plugin
//    id("org.jetbrains.intellij") version "1.10.0"
//    // Gradle Changelog Plugin
//    id("org.jetbrains.changelog") version "1.3.1"
//    // Gradle Qodana Plugin
//    id("org.jetbrains.qodana") version "0.1.13"
//}

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    plugins.set(listOf("com.intellij.clion"))
    version.set(properties("platformVersion"))
    type.set("CL")
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    //version = properties("pluginVersion")
    version.set(properties("pluginVersion"))
    groups.set(emptyList())

    path.set("${project.projectDir}/CHANGELOG.md")
    //groups = emptyList()
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
}

tasks {
    // Set the compatibility versions to 17
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }


    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            File(projectDir, "README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )
    }


    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

//tasks {
//    // Set the compatibility versions to 17
//    withType<JavaCompile> {
//        sourceCompatibility = "17"
//        targetCompatibility = "17"
//    }
//    withType<KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }
//
//    patchPluginXml {
//        version.set(properties("pluginVersion"))
//        sinceBuild.set(properties("pluginSinceBuild"))
//        untilBuild.set(properties("pluginUntilBuild"))
//
//        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
//        pluginDescription.set(
//            File(projectDir, "README.md").readText().lines().run {
//                val start = "<!-- Plugin description -->"
//                val end = "<!-- Plugin description end -->"
//
//                if (!containsAll(listOf(start, end))) {
//                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
//                }
//                subList(indexOf(start) + 1, indexOf(end))
//            }.joinToString("\n").run { markdownToHTML(this) }
//        )
//
//        // Get the latest available change notes from the changelog file
//        changeNotes.set(provider { changelog.getLatest().toHTML() })
//    }
//
//    runPluginVerifier {
//        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
//    }
//

//}
