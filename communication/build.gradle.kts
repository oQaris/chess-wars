dependencies {
    implementation(project(":core"))

    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation(project(mapOf("path" to ":core")))
}