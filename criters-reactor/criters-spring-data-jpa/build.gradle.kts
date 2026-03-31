dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api("org.springframework.data:spring-data-jpa:${rootProject.extra["springDataJpaVersion"]}")
    api("javax.persistence:javax.persistence-api:${rootProject.extra["javaPersistenceApiVersion"]}")
    api("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    api("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")

    testImplementation("junit:junit:${rootProject.extra["junitVersion"]}")
    testImplementation("org.hamcrest:hamcrest-core:${rootProject.extra["hamcrestVersion"]}")
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
    testImplementation(project(":criters-test-core"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation("org.hibernate:hibernate-entitymanager:${rootProject.extra["hibernateCoreVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa:${rootProject.extra["springBootVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${rootProject.extra["springBootVersion"]}")
}
