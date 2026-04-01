import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.spring.dependency-management") version "1.1.4"
}

group = "se.oyabun.criters"
version = "1.0.2-SNAPSHOT"

// Shared dependency versions
extra["javaVersion"] = "21"
extra["jakartaPersistenceApiVersion"] = "3.1.0"
extra["hibernateCoreVersion"] = "6.4.4.Final"
extra["eclipselinkVersion"] = "4.0.2"
extra["commonsLangVersion"] = "3.4"
extra["slf4jApiVersion"] = "1.7.21"
extra["logbackClassicVersion"] = "1.1.7"
extra["junitJupiterVersion"] = "5.10.2"
extra["hamcrestVersion"] = "2.2"
extra["mockitoCoreVersion"] = "5.11.0"
extra["h2Version"] = "2.2.224"
extra["p6spyVersion"] = "3.9.1"
extra["springBootVersion"] = "3.2.4"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "io.spring.dependency-management")

    group = "se.oyabun.criters"
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    val springBootVersion = rootProject.extra["springBootVersion"].toString()
    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                pom {
                    name = "${rootProject.group}:${project.name}"
                    description = "Criters engine is a framework for automating searches using JPA."
                    url = "https://github.com/OyabunAB/criters/"

                    licenses {
                        license {
                            name = "Apache License, Version 2.0"
                            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                            distribution = "repo"
                        }
                    }

                    developers {
                        developer {
                            id = "dansun"
                            name = "Daniel Sundberg"
                            email = "daniel.sundberg@oyabun.se"
                            organization = "Oyabun"
                            organizationUrl = "http://www.oyabun.se"
                            roles = listOf("Software Architect", "Developer")
                            timezone = "GMT+1"
                        }
                    }

                    scm {
                        url = "https://github.com/OyabunAB/criters/"
                        connection = "scm:git:git://github.com/OyabunAB/criters.git"
                        developerConnection = "scm:git:git@github.com:OyabunAB/criters.git"
                    }
                }
            }
        }

        repositories {
            maven {
                name = "sonatype"
                url = if (version.toString().endsWith("SNAPSHOT")) {
                    URI("https://oss.sonatype.org/content/repositories/snapshots")
                } else {
                    URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                }

                credentials {
                    username = project.findProperty("ossrhUsername")?.toString()
                    password = project.findProperty("ossrhPassword")?.toString()
                }
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
    }
}

// Exclude test-only modules from publishing
configure(listOf(
    project(":criters-test-eclipselink"),
    project(":criters-test-hibernate")
)) {
    publishing.publications.clear()
    tasks.withType<Sign>().all { enabled = false }
}
