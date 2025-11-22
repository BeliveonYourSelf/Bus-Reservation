package com.bus.reservation.data.di

import android.content.Context
import androidx.work.WorkManager
import com.bus.reservation.comman.SharePrefranceUtills
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiKey(): String = SharePrefranceUtills.APIKEY

    @Provides
    fun provideOkHttpsClient(apiKey: String): OkHttpClient {
        val httpsLoginIntercerp = HttpLoggingInterceptor()
        httpsLoginIntercerp.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpsLoginIntercerp.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val requestInterceptor = Interceptor { chain ->
            val newUrl = chain
                .request()
                .url
                .newBuilder()
                .addQueryParameter("x_cg_demo_api_key", "Constants.API_KEY")
                .build()

            val request = chain
                .request()
                .newBuilder()
//                .url(newUrl)
                .addHeader("x-access-token", "${SharePrefranceUtills.oAuthToken}")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        val trustAllCertificates = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf<X509Certificate>()
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustAllCertificates), SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates)
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpsLoginIntercerp)
            .connectTimeout(2000, TimeUnit.SECONDS)
            .readTimeout(2000, TimeUnit.SECONDS)
            .writeTimeout(2000, TimeUnit.SECONDS)
            .hostnameVerifier { hostname, session -> true }
            .retryOnConnectionFailure(true)
//            .followRedirects(true)
//            .followSslRedirects(true)
            .build()

    }

    @Provides
    @Singleton
    fun provideRetrofitClient(apiKey: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiKey)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHostSelectionInterceptor(
        preferenceHelper: String,
    ): HostSelectionInterceptor {
        return HostSelectionInterceptor(preferenceHelper)
    }

    @Provides
    @Singleton
    fun proivdeWorkManger(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}