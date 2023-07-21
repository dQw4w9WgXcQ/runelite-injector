plugins {
    kotlin("jvm") version "1.9.0"
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")
}

//i forget if this is necessary, but might be needed so the jdk classes loaded for COMPUTE_FRAMES is consistent
kotlin {
    jvmToolchain(11)
}

tasks {
    register<JavaExec>("inject") {
        dependsOn(":mixins:jar")

        classpath(sourceSets["main"].runtimeClasspath)
        mainClass.set("dqw4w9wgxcq.runeliteinjector.Main")
    }
}