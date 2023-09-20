dependencies {
    implementation(project(":core"))
    implementation(project(":server"))
    implementation(project(":client"))
    implementation(project(":communication"))
    implementation("com.intellij:forms_rt:7.0.3")
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application.mainClass = "gui.GameManager"