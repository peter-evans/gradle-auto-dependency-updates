import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    val kotlinVersion = "1.3.21"
    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    val dropwizardVersion = "2.0.7"
    implementation("io.dropwizard:dropwizard-core:$dropwizardVersion")
    implementation("io.dropwizard:dropwizard-testing:$dropwizardVersion")

    val junitVersion = "5.6.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // The following dependencies are unused in this example project and are
    // listed here for demonstrating dependency updates only.
    val awsVersion = "2.11.12"
    implementation("software.amazon.awssdk:dynamodb:$awsVersion")
    implementation("software.amazon.awssdk:sqs:$awsVersion")
    val coroutineVersion = "1.3.5"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    testImplementation("io.mockk:mockk:1.9.3")
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
