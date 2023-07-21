plugins {
    id("java")
}

dependencies {
    val rlVersion = "1.10.8.2"
    compileOnly("net.runelite:runelite-api:$rlVersion")
    implementation(files("libs/allatori-annotations.jar"))
}

java {
    disableAutoTargetJvm()
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
}