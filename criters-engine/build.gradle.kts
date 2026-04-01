dependencies {
    api(project(":criters-annotation"))
    api(libs.jakartaPersistenceApi)
    api(libs.commonsLang3)
    api(libs.slf4jApi)

    testImplementation(project(":criters-test-core"))
    testImplementation(libs.junitJupiter)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoJupiter)
    testImplementation(libs.logbackClassic)
}
