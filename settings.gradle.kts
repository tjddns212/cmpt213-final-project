pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven{ url= uri("https://jitpack.io")}
    }
}


rootProject.name = "Learn Lock"
include(":app")
