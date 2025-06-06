plugins {
    id("java")
    id("io.freefair.lombok") version "8.6" // Auto-configures Lombok
}

group = "me.rejomy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }

    // Add flat directory for local libs
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Add all JAR files from libs directory as dependencies
    fileTree("libs").forEach { file ->
        if (file.name.endsWith(".jar")) {
            implementation(files(file.path))
        }
    }

    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")
    compileOnly("org.projectlombok:lombok:1.18.30") // Compile-time only
    annotationProcessor("org.projectlombok:lombok:1.18.30") // Annotation processing
}