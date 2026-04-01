group = "se.oyabun.criters.test"

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(project(":criters-test-core"))
    api(libs.junitJupiter)
    api(libs.hamcrest)
    api(libs.h2)
    api(libs.p6spy)
}
