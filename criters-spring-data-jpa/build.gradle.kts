import java.net.URI

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

publishing {
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
                        organizationUrl = "https://www.oyabun.se"
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
