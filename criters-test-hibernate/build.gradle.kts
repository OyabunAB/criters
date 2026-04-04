plugins {
    alias(libs.plugins.java.library)
}

group = "se.oyabun.criters.test"

// This module only contains test code, no main sources
sourceSets {
    main {
        java.setSrcDirs(emptySet<String>())
        resources.setSrcDirs(emptySet<String>())
    }
}

dependencies {
    testImplementation(project(":criters-engine"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.hibernate.core)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.logback.classic)
}
