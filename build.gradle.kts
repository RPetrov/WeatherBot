plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // see https://github.com/pengrad/java-telegram-bot-api
    implementation("com.github.pengrad:java-telegram-bot-api:6.3.0")

    // see https://www.baeldung.com/apache-commons-csv
    implementation("org.apache.commons:commons-csv:1.10.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}