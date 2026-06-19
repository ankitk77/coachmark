plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "io.github.pseudoankit"
    version = (System.getenv("RELEASE_TAG_NAME") ?: "1.7.1-SNAPSHOT").replace("v", "")
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            stagingProfileId.set(System.getenv("OSS_STAGING_PROFILE_ID"))
            username.set(System.getenv("OSS_USERNAME"))
            password.set(System.getenv("OSS_PASSWORD"))
        }
    }
}
