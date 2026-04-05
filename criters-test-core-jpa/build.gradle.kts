plugins {
    alias(libs.plugins.java.library)
}

group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-test-core"))
    api(libs.junit.jupiter)
    api(libs.hamcrest)
    api(libs.p6spy)
    api(libs.bundles.testing)
}
