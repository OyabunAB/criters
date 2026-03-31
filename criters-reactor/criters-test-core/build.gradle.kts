group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
    api("javax.persistence:javax.persistence-api:${rootProject.extra["javaPersistenceApiVersion"]}")
    api("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
}
