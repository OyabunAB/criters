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
    testImplementation("org.hibernate.orm:hibernate-core:${rootProject.extra["hibernateCoreVersion"]}")
    testImplementation("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    testImplementation("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
}
