import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.KotlinMultiplatform
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish") version "0.28.0"
    signing
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

// Read your local properties file manually to guarantee Gradle parses it
val properties = Properties().apply {
    val file = rootProject.file("gradle.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

signing {
    val keyId = properties.getProperty("signing.keyId")
    val password = properties.getProperty("signing.password")
    val secretKey = properties.getProperty("signing.secretKey")

    if (!keyId.isNullOrEmpty() && !secretKey.isNullOrEmpty()) {
        // Force-injects the keys cleanly directly into the signing engine
        useInMemoryPgpKeys(keyId, secretKey, password)
    }
}

mavenPublishing {
    // Coordinates
    coordinates(
        groupId = "io.github.ankitk77",
        artifactId = "coachmark",
        version = "4.0.0"
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
