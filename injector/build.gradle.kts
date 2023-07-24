plugins {
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.google.code.gson:gson:2.8.6")
}

tasks {
    register<JavaExec>("inject") {
        dependsOn(":mixins:jar")

        systemProperty("projectVersion", version)
        systemProperty("rlVersion", Versions.runelite)

        classpath(sourceSets["main"].runtimeClasspath)
        mainClass.set("dqw4w9wgxcq.runeliteinjector.Main")
    }
}