tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

plugins {
    id("java")
    id("net.ltgt.errorprone") version "3.1.0"
    id("io.freefair.lombok") version "8.2.2"
}

group = "io.deeplay"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "net.ltgt.errorprone")
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.junit.jupiter:junit-jupiter:5.8.1")
        errorprone("com.google.errorprone:error_prone_core:2.20.0")

        testImplementation(platform("org.junit:junit-bom:5.9.3"))
        testImplementation("org.junit.jupiter:junit-jupiter")

        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("com.google.code.gson:gson:2.10.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}