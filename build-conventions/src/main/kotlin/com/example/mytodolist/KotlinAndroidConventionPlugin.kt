package com.example.mytodolist


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinAndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }

            dependencies {
                "implementation"(libs.androidx.core.ktx)
                "implementation"(libs.androidx.appcompat)
                "implementation"(libs.material)
                "implementation"(libs.androidx.activity.ktx)
                "implementation"(libs.androidx.constraintlayout)
                "implementation"(libs.androidx.lifecycle.runtime.ktx)
                "implementation"(libs.androidx.activity.compose)
                "implementation"(platform(libs.androidx.compose.bom))
                "implementation"(libs.androidx.compose.ui)
                "implementation"(libs.androidx.compose.ui.graphics)
                "implementation"(libs.androidx.compose.ui.tooling.preview)
                "implementation"(libs.androidx.material3)
                "implementation"(libs.play.services.maps)
                "implementation"(libs.androidx.navigation.runtime.ktx)
                "implementation"(libs.androidx.navigation.compose)
                "implementation"(libs.retrofit)
                "implementation"(libs.retrofit.converter.gson)
                "implementation"(libs.okhttp)
                "implementation"(libs.okhttp.logging.interceptor)
                "implementation"(libs.firebase.dataconnect)
                "implementation"(libs.androidx.work.runtime.ktx)
                "implementation"(libs.dagger)
                "implementation"(libs.androidx.room.runtime)
                "implementation"(libs.androidx.room.ktx)
                "kapt"(libs.androidx.room.compiler)
                "kapt"(libs.dagger.compiler)
                "testImplementation"(libs.junit)
                "androidTestImplementation"(libs.androidx.junit)
                "androidTestImplementation"(libs.androidx.espresso.core)
                "androidTestImplementation"(platform(libs.androidx.compose.bom))
                "androidTestImplementation"(libs.androidx.compose.ui.test.junit4)
                "debugImplementation"(libs.androidx.compose.ui.tooling)
                "debugImplementation"(libs.androidx.compose.ui.test.manifest)
            }
        }
    }
}