group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(project(":criters-test-core"))
    api("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    api("org.hamcrest:hamcrest:${rootProject.extra["hamcrestVersion"]}")
    api("com.h2database:h2:${rootProject.extra["h2Version"]}")
    api("p6spy:p6spy:${rootProject.extra["p6spyVersion"]}")
}
