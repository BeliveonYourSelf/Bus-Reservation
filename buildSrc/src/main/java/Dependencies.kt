object Version {
    const val core_ktx = "1.15.0"
    const val appcompat = "1.7.0"
    const val material = "1.12.0"
    const val activity = "1.9.3"
    const val constraintlayout = "2.2.0"
    const val testImplJunit = "4.13.2"
    const val androidTestImplJunit = "1.2.1"
    const val androidTestEspresso = "3.6.1"
    const val retrofit = "2.11.0"
    const val scalerConvertor = "2.1.0"
    const val okHttp = "4.12.0"
    const val dagger = "2.51.1"
    const val hiltCompiler = "1.2.0"
    const val ssp = "1.1.1"
    const val sdp = "1.1.1"
    const val activity_ktx = "1.9.3"
    const val fragment_ktx = "1.8.5"
    const val lifecycle_version = "2.8.7"
    const val WorkManager_Version = "2.9.0"
    const val Hilt_WorkManger = "1.2.0"
    const val paging_version = "3.3.5"
}

// androidx.core:core-ktx:
object Dependencies {
    const val andoird_core = "androidx.core:core-ktx:${Version.core_ktx}"
    const val andoirdx_appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val andoirdx_material = "com.google.android.material:material:${Version.material}"
    const val androidx_activity = "androidx.activity:activity:${Version.activity}"
    const val androidx_constraintlayout = "androidx.constraintlayout:constraintlayout:${Version.constraintlayout}"

}

object TestImplementation {
    const val junit = "junit:junit:${Version.testImplJunit}"
}

object AndroidTestImplementation {
    const val junit = "androidx.test.ext:junit:${Version.androidTestImplJunit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.androidTestEspresso}"

}

object SSP {
    const val sdp = "com.intuit.sdp:sdp-android:${Version.sdp}"
    const val ssp = "com.intuit.ssp:ssp-android:${Version.ssp}"
}

object ActivityDeleget {
    const val androidx_activity = "androidx.activity:activity-ktx:${Version.activity_ktx}"
    const val androidx_fragments = "androidx.fragment:fragment-ktx:${Version.fragment_ktx}"
}

object ViewModelAndLiveData {
    const val viewModels = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle_version}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle_version}"
}

object Retrofit {
    const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val RetrofitgsonConvertor = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val scalersConvertors =
        "com.squareup.retrofit2:converter-scalars:${Version.scalerConvertor}"
}

object OkHttps {
    const val okHttpClient = "com.squareup.okhttp3:okhttp:${Version.okHttp}"
    const val okHttpClient_Login_InterCeptor =
        "com.squareup.okhttp3:logging-interceptor:${Version.okHttp}"
}

object DaggerHilt {
    const val Daggerhilt = "com.google.dagger:hilt-android:${Version.dagger}"
    const val DaggerhiltAndroidCompiler =
        "com.google.dagger:hilt-android-compiler:${Version.dagger}"
    const val OnlyhiltCompiler = "androidx.hilt:hilt-compiler:${Version.hiltCompiler}"
}

object WorkManager {
    const val WorkManagerRunTime = "androidx.work:work-runtime-ktx:${Version.WorkManager_Version}"
    const val HiltWorkManager = "androidx.hilt:hilt-work:${Version.Hilt_WorkManger}"
    const val HiltCompilerOfManager = "androidx.hilt:hilt-compiler:${Version.Hilt_WorkManger}"
}

object Paging3 {
    const val Paging3AndroidRunTime = "androidx.paging:paging-runtime:${Version.paging_version}"
}


//object Coroutines {
//    const val coroutineCore =
//        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinCoroutines}"
//    const val coroutineAndroid =
//        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlinCoroutines}"
//}

//object CoroutinesLifecycleScope {
//    const val lifecycleViewModel =
//        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.coroutineLifecycleScope}"
//    const val lifeCycleRuntime =
//        "androidx.lifecycle:lifecycle-runtime-ktx:${Version.coroutineLifecycleScope}"
//}

//object Glide {
//    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
//    const val annotationProcessor = "com.github.bumptech.glide:compiler:${Version.glide}"
//}

//object ViewModelDelegate {
//    const val viewModelDeligate = "androidx.activity:activity-ktx:${Version.viewModelDeligate}"
//}





