tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

plugins {
    id("java")
    id("net.ltgt.errorprone") version "3.1.0"
}

group = "io.deeplay"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "net.ltgt.errorprone")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        errorprone("com.google.errorprone:error_prone_core:2.20.0")

        testImplementation(platform("org.junit:junit-bom:5.9.3"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
