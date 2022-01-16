pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "Last News App"
//DataLayer
include(":app")

//For dependency management, we use a gradle plugin
includeBuild("gradlePlugins")
