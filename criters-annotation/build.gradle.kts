import java.net.URI

plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

dependencies {
    api(libs.bundles.jpa.common)
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
