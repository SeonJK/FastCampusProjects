import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.seonjk.mygithubrepo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seonjk.mygithubrepo"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val githubClientId: String = gradleLocalProperties(rootDir).getProperty("GITHUB_CLIENT_ID")
        buildConfigField("String", "GITHUB_CLIENT_ID", "\"$githubClientId\"")

        val githubClientSecret: String = gradleLocalProperties(rootDir).getProperty("GITHUB_CLIENT_SECRET")
        buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$githubClientSecret\"")
    }

    buildFeatures {
        buildConfig = true

        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // KSP
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // androidx.browser
    implementation("androidx.browser:browser:1.8.0")

    // OkHttp3
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    // SharedPreferences
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Room DB
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Glide
    val glide_version = "4.14.2"
    implementation("com.github.bumptech.glide:glide:$glide_version")
    ksp("com.github.bumptech.glide:ksp:$glide_version")

}