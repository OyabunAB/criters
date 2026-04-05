plugins {
    alias(libs.plugins.java.library)
}

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(libs.spring.data.jpa)

    testImplementation(project(":criters-test-core"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.hibernate.core)
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.spring.testing)
}
