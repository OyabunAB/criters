plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
}
