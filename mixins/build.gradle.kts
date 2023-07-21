plugins {
    id("java")
}

dependencies {
    compileOnly("net.runelite:runelite-api:${Versions.runelite}")
    implementation(files("libs/allatori-annotations.jar"))
}

java {
    disableAutoTargetJvm()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
}