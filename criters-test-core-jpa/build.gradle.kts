plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(project(":criters-test-core"))
    api(libs.junit.jupiter)
    api(libs.hamcrest)
    api(libs.postgresql)
    api(libs.p6spy)
    api(libs.bundles.testcontainers)
}
