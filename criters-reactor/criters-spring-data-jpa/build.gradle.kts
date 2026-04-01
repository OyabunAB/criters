dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api("org.springframework.data:spring-data-jpa")
    api("jakarta.persistence:jakarta.persistence-api:${rootProject.extra["jakartaPersistenceApiVersion"]}")
    api("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    api("org.slf4j:slf4j-api:${rootProject.extra["slf4jApiVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    testImplementation("org.hamcrest:hamcrest:${rootProject.extra["hamcrestVersion"]}")
    testImplementation("org.mockito:mockito-core:${rootProject.extra["mockitoCoreVersion"]}")
    testImplementation("org.mockito:mockito-junit-jupiter:${rootProject.extra["mockitoCoreVersion"]}")
    testImplementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackClassicVersion"]}")
    testImplementation(project(":criters-test-core"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation("org.hibernate.orm:hibernate-core:${rootProject.extra["hibernateCoreVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
