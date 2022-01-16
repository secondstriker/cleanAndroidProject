package com.codewithmohsen.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependenciesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
    }

    companion object{

        const val TargetSdk = 31
        const val CompileSdk = 31
        const val MinSdk = 19

        const val AndroidxCore = "androidx.core:core-ktx:1.6.0"
        const val AndroidxAppCompat = "androidx.appcompat:appcompat:1.3.1"
        const val AndroidxJUnit = "androidx.test.ext:junit:1.1.3"
        const val AndroidxEspresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val AndroidXArchCoreTest = "androidx.arch.core:core-testing:2.1.0"
        const val AndroidXLifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:2.4.0"
        const val AndroidXLifecycleLiveDataKTX = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
        const val AndroidXLifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.4.0"
        const val AndroidXLifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:2.4.0"

        const val AndroidXHiltCompiler = "androidx.hilt:hilt-compiler:1.0.0"

        const val Material = "com.google.android.material:material:1.4.0"
        const val JUnit = "junit:junit:4.13.2"

        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

        const val Retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val RetrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:2.9.0"

        const val Timber = "com.jakewharton.timber:timber:5.0.1"

        const val Moshi = "com.squareup.moshi:moshi:1.12.0"
        const val MoshiKotlin = "com.squareup.moshi:moshi-kotlin:1.12.0"
        const val MoshiCodeGenerator = "com.squareup.moshi:moshi-kotlin-codegen:1.12.0"

        const val Mockito = "org.mockito:mockito-core:3.12.4"

        const val Okhttp = "com.squareup.okhttp3:okhttp:4.9.1"
        const val OkhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        const val OkhttpMockWebServer = "com.squareup.okhttp3:mockwebserver:4.9.1"

        const val KotlinXCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
        const val KotlinXCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
        const val KotlinXCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2"

        const val MultiDexApplication = "androidx.multidex:multidex:2.0.1"

        const val Json = "org.json:json:20210307"

        const val PreferencesKotlin = "androidx.preference:preference-ktx:1.1.1"
        const val PreferencesJava =  "androidx.preference:preference:1.1.1"

        const val HiltAndroid = "com.google.dagger:hilt-android:2.38.1"
        const val HiltCompiler = "com.google.dagger:hilt-compiler:2.38.1"
        const val HiltAndroidTesting = "com.google.dagger:hilt-android-testing:2.38.1"
        const val HiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.38.1"

        const val KotlinXSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
        const val Gson = "com.google.code.gson:gson:2.8.8"
        
        
        const val Gson = "com.google.code.gson:gson:2.8.8"

        const val Glide = "com.github.bumptech.glide:glide:4.12.0"
        const val GlideKtx = "com.github.bumptech.glide:compiler:4.12.0"
    }
}