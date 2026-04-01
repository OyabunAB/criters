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
    testImplementation(libs.hibernateCore)
    testImplementation(libs.commonsLang3)
    testImplementation(libs.slf4jApi)
    testImplementation(libs.junitJupiter)
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.logbackClassic)
}
