pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LinguaFlow"
include(":app")
include (":core")
include (":core:designsystem")
include (":core:common")
include (":domain")
include (":data")
include (":data:database")
include (":data:datastore")
include (":feature")
include (":feature:home")
include (":feature:games")
include (":feature:exercises")
