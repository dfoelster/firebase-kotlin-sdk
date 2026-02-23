package utils

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

enum class TargetPlatform {
    Android, Ios, Macos, Tvos, Jvm, Js
}

fun String.toTargetPlatforms(): List<TargetPlatform> =
    split(",").map {
        when (it.lowercase().trim()) {
            "android" -> TargetPlatform.Android
            "ios" -> TargetPlatform.Ios
            "macos" -> TargetPlatform.Macos
            "tvos" -> TargetPlatform.Tvos
            "jvm" -> TargetPlatform.Jvm
            "js" -> TargetPlatform.Js
            else -> throw IllegalArgumentException("Unknown target platform: $it")
        }
    }

fun List<TargetPlatform>.supportsApple() = this.any {
    it == TargetPlatform.Ios || it == TargetPlatform.Macos || it == TargetPlatform.Tvos
}

fun RepositoryHandler.addNexusRepo(project: Project) {

    maven {
        name = "NexusSnapshots"
        url = URI(project.property("deploySnapshotRepository") as String)

        credentials {
            username = project.property("nexusUsername") as? String ?: error("nexusUsername not set")
            password = project.property("nexusPassword") as? String ?: error("nexusPassword not set")
        }
    }
}