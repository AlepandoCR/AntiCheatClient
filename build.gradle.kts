plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "dev.alepando"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

application {
    mainClass.set("dev.alepando.ClientApp")
}