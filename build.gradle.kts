import java.net.URI
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

group = "se.oyabun.criters"
version = "1.0.2"

subprojects {
    group = "se.oyabun.criters"
    version = "1.0.2"

    repositories {
        mavenCentral()
    }

    pluginManager.withPlugin("org.gradle.java-library") {
        configure<JavaPluginExtension> {
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
            environment("DOCKER_HOST", "unix:///var/run/docker.sock")
            environment("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", "/var/run/docker.sock")
            environment("DOCKER_API_VERSION", "1.45")
        }
    }

    pluginManager.withPlugin("org.gradle.maven-publish") {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])

                    pom {
                        name = "${group}:${project.name}"
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

        pluginManager.withPlugin("org.gradle.signing") {
            configure<SigningExtension> {
                sign(extensions.getByType<PublishingExtension>().publications["mavenJava"])
            }
        }
    }
}
