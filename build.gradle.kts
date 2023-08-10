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
    apply(plugin = "java")

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
        implementation ("io.netty:netty-all:4.1.96.Final")
        implementation("org.slf4j:slf4j-api:2.0.7")
        compileOnly("org.projectlombok:lombok:1.18.28")
        implementation("com.google.code.gson:gson:2.10.1")

        implementation ("com.fasterxml.jackson.core:jackson-core:2.15.1")
        implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.1")
        implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}