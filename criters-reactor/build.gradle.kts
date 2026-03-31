import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "se.oyabun.criters"
version = "1.0.2-SNAPSHOT"

// Shared dependency versions
extra["javaVersion"] = "1.8"
extra["javaPersistenceApiVersion"] = "2.2"
extra["hibernateCoreVersion"] = "5.2.10.Final"
extra["eclipselinkVersion"] = "2.5.2"
extra["commonsLangVersion"] = "3.4"
extra["slf4jApiVersion"] = "1.7.21"
extra["logbackClassicVersion"] = "1.1.7"
extra["junitVersion"] = "4.12"
extra["mockitoVersion"] = "1.10.19"
extra["hamcrestVersion"] = "1.2.1"
extra["h2Version"] = "1.4.193"
extra["p6spyVersion"] = "3.2.0"
extra["springDataJpaVersion"] = "1.11.6.RELEASE"
extra["springBootVersion"] = "1.5.6.RELEASE"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    group = "se.oyabun.criters"
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
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
