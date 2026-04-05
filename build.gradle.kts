plugins {
    alias(libs.plugins.java.library)
}

group = "se.oyabun.criters"
version = findProperty("releaseVersion") ?: "0.0.0"

subprojects {
    apply(plugin = "java-library")

    group = "se.oyabun.criters"
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        toolchain { languageVersion = JavaLanguageVersion.of(21) }
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

}
