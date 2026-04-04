plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

dependencies {
    api(project(":criters-annotation"))
    api(project(":criters-engine"))
    api(libs.spring.data.jpa)

    testImplementation(project(":criters-test-core"))
    testImplementation(project(":criters-test-core-jpa"))
    testImplementation(libs.bundles.testing)
    testImplementation(libs.hibernate.core)
    testImplementation(libs.postgresql)
    testImplementation(libs.bundles.testcontainers)
    testImplementation(libs.bundles.spring.testing)
}
