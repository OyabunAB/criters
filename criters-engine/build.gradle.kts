plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

dependencies {
    api(project(":criters-annotation"))

    testImplementation(project(":criters-test-core"))
    testImplementation(libs.bundles.testing)
}
