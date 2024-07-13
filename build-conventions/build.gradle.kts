plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google() // Add this if you are using Google's Maven repository
}

dependencies {
    implementation("com.android.tools.build:gradle:8.0.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
}