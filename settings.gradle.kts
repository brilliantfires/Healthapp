pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://www.jitpack.io") }
        maven { setUrl("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://www.jitpack.io") }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Healthapp"
include(":app")
 