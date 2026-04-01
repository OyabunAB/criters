dependencies {
    api(project(":criters-annotation"))
    api("jakarta.persistence:jakarta.persistence-api:${rootProject.extra["jakartaPersistenceApiVersion"]}")
    api("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    api("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")

    testImplementation(project(":criters-test-core"))
    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    testImplementation("org.hamcrest:hamcrest:${rootProject.extra["hamcrestVersion"]}")
    testImplementation("org.mockito:mockito-core:${rootProject.extra["mockitoCoreVersion"]}")
    testImplementation("org.mockito:mockito-junit-jupiter:${rootProject.extra["mockitoCoreVersion"]}")
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
}
