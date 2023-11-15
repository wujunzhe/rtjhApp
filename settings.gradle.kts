pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://maven.aliyun.com/repository/public"))
        maven(uri("https://jitpack.io"))
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "rtjhApp"
include(":app")
 