plugins {
    alias(libs.plugins.java.library)
}

group = "se.oyabun.criters.test"

sourceSets {
    main {
        java.setSrcDirs(emptySet<String>())
        resources.setSrcDirs(emptySet<String>())
    }
}

dependencies {
    testImplementation(project(":criters-engine"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.eclipselink)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.logback.classic)
    testRuntimeOnly(libs.junit.platform.launcher)
}
