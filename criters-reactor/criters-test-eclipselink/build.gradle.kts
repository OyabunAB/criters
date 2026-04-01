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
    testImplementation("org.eclipse.persistence:eclipselink:${rootProject.extra["eclipselinkVersion"]}")
    testImplementation("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
}
