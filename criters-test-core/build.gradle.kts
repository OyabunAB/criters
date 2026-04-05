plugins {
    alias(libs.plugins.java.library)
}

group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    testImplementation(libs.bundles.testing)
}
