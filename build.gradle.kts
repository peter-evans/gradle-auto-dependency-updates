import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    // This plugin dependency is unused in this example project and is
    // listed here for demonstrating dependency updates only.
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.+")
    }
    configurations.classpath {
        resolutionStrategy.activateDependencyLocking()
    }
}

apply(plugin = "com.jfrog.bintray")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    java
    application
}

group = "com.github.peter-evans"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kotlinVersion = "1.3.+"
    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    val dropwizardVersion = "2.0.+"
    implementation("io.dropwizard:dropwizard-core:$dropwizardVersion")
    implementation("io.dropwizard:dropwizard-testing:$dropwizardVersion")

    val junitVersion = "5.6.+"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // The following dependencies are unused in this example project and are
    // listed here for demonstrating dependency updates only.
    val awsVersion = "2.11.+"
    implementation("software.amazon.awssdk:dynamodb:$awsVersion")
    implementation("software.amazon.awssdk:sqs:$awsVersion")
    val coroutineVersion = "1.3.+"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    testImplementation("io.mockk:mockk:1.+")
}

dependencyLocking {
    lockAllConfigurations()
}

application {
    mainClassName = "com.github.peterevans.exampleapi.ExampleApiApplication"
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    "test"(Test::class) {
        useJUnitPlatform()
    }

    named<JavaExec>("run") {
        args("server", "./config/local.yml")
    }
}

tasks.register("resolveAndLockAll") {
    doFirst {
        require(gradle.startParameter.isWriteDependencyLocks)
    }
    doLast {
        configurations.filter {
            it.isCanBeResolved
        }.forEach { it.resolve() }
    }
}
