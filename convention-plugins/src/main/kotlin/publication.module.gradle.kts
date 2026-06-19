import java.util.Base64

plugins {
    `maven-publish`
    signing
}

val projectUrl = "https://github.com/pseudoankit/coachmark"

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("Compose Coachmark")
            description.set("A Compose multiplatform library to create onboarding flow / coachmark.")
            url.set(projectUrl)

            licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            issueManagement {
                system.set("Github")
                url.set("$projectUrl/issues")
            }
            scm {
                connection.set("$projectUrl.git")
                url.set(projectUrl)
            }
            developers {
                developer {
                    id.set("pseudoankit")
                    name.set("Ankit Kumar")
                    email.set("lostankit7@gmail.com")
                }
            }
        }
    }
}

val signingKeyId = System.getenv("OSS_SIGNING_KEY_ID").orEmpty()
val signingKey = System.getenv("OSS_SIGNING_KEY")
    ?.replace("\\n", "\n")
    ?.let { key ->
        runCatching {
            String(Base64.getDecoder().decode(key))
        }.getOrNull()
            ?.takeIf { decodedKey -> decodedKey.contains("BEGIN PGP PRIVATE KEY BLOCK") }
            ?: key
    }
val signingPassword = System.getenv("OSS_SIGNING_PASSWORD")

signing {
    if (signingKey.isNullOrBlank()) {
        logger.warn("OSS_SIGNING_KEY is not set; publishing tasks will fail until signing secrets are provided.")
    }

    if (signingKeyId.isBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    } else {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    }

    sign(publishing.publications)
}

tasks.withType<Sign>().configureEach {
    doFirst {
        if (signingKey.isNullOrBlank()) {
            throw GradleException("Missing OSS_SIGNING_KEY. Export it locally or configure the GitHub Actions secret before publishing.")
        }
        if (signingPassword.isNullOrBlank()) {
            throw GradleException("Missing OSS_SIGNING_PASSWORD. Export it locally or configure the GitHub Actions secret before publishing.")
        }
    }
}

// TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}
