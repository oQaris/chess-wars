dependencies{
    implementation(project(":core"))
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":communication")))
}