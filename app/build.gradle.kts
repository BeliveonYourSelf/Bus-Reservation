plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.bus.reservation"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bus.reservation"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding=true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(Dependencies.andoird_core)
    implementation(Dependencies.andoirdx_appcompat)
    implementation(Dependencies.andoirdx_material)
    implementation(Dependencies.androidx_activity)
    implementation(Dependencies.androidx_constraintlayout)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(TestImplementation.junit)
    androidTestImplementation(AndroidTestImplementation.junit)
    androidTestImplementation(AndroidTestImplementation.espresso)


//    implementation(libs.ssp.android)
//    implementation(libs.sdp.android)
    implementation(SSP.sdp)
    implementation(SSP.ssp)
    
    implementation(DaggerHilt.Daggerhilt)
    ksp(DaggerHilt.DaggerhiltAndroidCompiler)
    ksp(DaggerHilt.OnlyhiltCompiler)

//    implementation(libs.hilt.android)
//    ksp(libs.hilt.android.compiler)


    // define any required OkHttp artifacts without version
//    implementation("com.squareup.okhttp3:okhttp")
//    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation(OkHttps.okHttpClient)
    implementation(OkHttps.okHttpClient_Login_InterCeptor)

//    implementation("com.squareup.retrofit2:retrofit:2.11.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation(Retrofit.Retrofit)
    implementation(Retrofit.RetrofitgsonConvertor)

//    implementation("androidx.activity:activity-ktx:1.9.3")
//    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation(ActivityDeleget.androidx_activity)
    implementation(ActivityDeleget.androidx_fragments)

//    val lifecycle_version = "2.8.7"
//    // ViewModel
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation(ViewModelAndLiveData.viewModels)
    implementation(ViewModelAndLiveData.liveData)
    // LiveData
    implementation("androidx.compose.runtime:runtime:1.7.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    implementation(WorkManager.WorkManagerRunTime)
    implementation(WorkManager.HiltWorkManager)
    ksp(WorkManager.HiltCompilerOfManager)


    implementation(Paging3.Paging3AndroidRunTime)

    implementation ("com.airbnb.android:lottie:6.6.2")

    implementation ("com.vipulasri:ticketview:1.1.2")
}