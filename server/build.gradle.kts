dependencies{
    implementation(project(":core"))
    implementation(project(":client"))
    implementation(project(":communication"))

    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application.mainClass = "io.deeplay.server.Server"