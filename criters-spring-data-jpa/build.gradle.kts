dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(libs.springDataJpa)
    api(libs.jakartaPersistenceApi)
    api(libs.commonsLang3)
    api(libs.slf4jApi)

    testImplementation(libs.junitJupiter)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoJupiter)
    testImplementation(libs.logbackClassic)
    testImplementation(project(":criters-test-core"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.hibernateCore)
    testImplementation(libs.springBootStarterDataJpa)
    testImplementation(libs.springBootStarterTest)
}
