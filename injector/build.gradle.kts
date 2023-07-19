plugins { kotlin("jvm") version "1.9.0" }

group = "dev.dqw4w9wgxcq"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.runelite.net") }
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

kotlin { jvmToolchain(11) }

tasks {
    test { useJUnitPlatform() }

    register<JavaExec>("inject") {
        classpath(sourceSets["main"].runtimeClasspath)
        mainClass.set("dqw4w9wgxcq.runeliteinjector.Main")
        dependsOn(":mixins:jar")
    }
}