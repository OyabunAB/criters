dependencies {
    api(project(":criters-annotation"))
    api("javax.persistence:javax.persistence-api:${rootProject.extra["javaPersistenceApiVersion"]}")
    api("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    api("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")

    testImplementation(project(":criters-test-core"))
    testImplementation("junit:junit:${rootProject.extra["junitVersion"]}")
    testImplementation("org.mockito:mockito-all:${rootProject.extra["mockitoVersion"]}")
    testImplementation("org.hamcrest:hamcrest-core:${rootProject.extra["hamcrestVersion"]}")
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
}
