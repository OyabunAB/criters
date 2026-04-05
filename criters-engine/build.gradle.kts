plugins {
    alias(libs.plugins.java.library)
}

dependencies {
    api(project(":criters-annotation"))
    testImplementation(project(":criters-test-core"))
    testImplementation(libs.bundles.testing)
}
