plugins { id("java") }

group = "dev.dqw4w9wgxcq"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.runelite.net") }
    flatDir { dirs("libs") }
}

java {
    disableAutoTargetJvm()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val rlVersion = "1.10.8.2"
    compileOnly("net.runelite:runelite-api:$rlVersion")
    implementation(files("libs/allatori-annotations.jar"))
}

tasks {
}