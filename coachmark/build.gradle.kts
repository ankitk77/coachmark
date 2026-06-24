import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("publication.module")
    id("com.vanniktech.maven.publish")
}

composeCompiler {
    enableStrongSkippingMode = true
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    jvm("desktop")
    js(IR) {
        browser()
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            this.baseName = baseName
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }

    explicitApi()
}

android {
    namespace = "com.pseudoankit.coachmark"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    // Coordinates
    coordinates(
        groupId = "io.github.ankitk77",
        artifactId = "coachmark",
        version = "3.0.8"
    )

    // POM configuration
    pom {
        name.set("Coachmark")
        description.set("A lightweight compose multiplatform library to create better onboarding experiences.")
        inceptionYear.set("2024")
        url.set("https://github.com/ankitk77/coachmark")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("ankitk77")
                name.set("Ankit Kumar")
            }
        }

        scm {
            url.set("https://github.com/ankitk77/coachmark")
            connection.set("scm:git:git://github.com/ankitk77/coachmark.git")
            developerConnection.set("scm:git:ssh://github.com/ankitk77/coachmark.git")
        }
    }

    // Configure the target to publish all KMP targets automatically
    configure(KotlinMultiplatform(javadocJar = com.vanniktech.maven.publish.JavadocJar.Empty()))

    // Publish to the NEW Sonatype Central portal
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)

    // Enable GPG signing
    signAllPublications()
}
