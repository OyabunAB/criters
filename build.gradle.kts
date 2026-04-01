import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.springDependencyManagement)
}

group = "se.oyabun.criters"
version = "1.0.2-SNAPSHOT"

val springBootDependenciesBom = libs.springBootDependencies.get().toString()

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

    dependencyManagement {
        imports {
            mavenBom(springBootDependenciesBom)
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
            maven {
                name = "github"
                url = URI("https://maven.pkg.github.com/OyabunAB/criters")
                credentials {
                    username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("github.user")?.toString()
                    password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("github.token")?.toString()
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
