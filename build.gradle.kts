// Тестовый коммит
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
    implementation("com.github.pengrad:java-telegram-bot-api:6.8.0")

    // see https://www.baeldung.com/apache-commons-csv
    implementation("org.apache.commons:commons-csv:1.10.0")

    testImplementation(kotlin("test"))

    // see https://github.com/Prominence/openweathermap-java-api/blob/master/docs/Release_2.3.0.md
    implementation("com.github.prominence:openweathermap-api:2.3.0")
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