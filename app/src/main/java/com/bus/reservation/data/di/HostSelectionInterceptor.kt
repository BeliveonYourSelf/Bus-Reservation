package com.bus.reservation.data.di

import com.vipulasri.ticketview.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.annotations.NotNull

class HostSelectionInterceptor(private val preferenceHelper: String) : Interceptor {

  private var host: HttpUrl? = "BuildConfig.DEVELOPMENT_BASE_URL".toHttpUrlOrNull()

  init {
    setHostBaseUrl()
  }

  private fun setHostBaseUrl() {
    host = if (true) {
      "BuildConfig.PRODUCTION_BASE_URL".
      toHttpUrlOrNull()
    } else {
      "BuildConfig.DEVELOPMENT_BASE_URL".toHttpUrlOrNull()
    }
  }

  @NotNull
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    if (host != null) {
      val newUrl = request.url.newBuilder()
          .scheme(host!!.scheme)
          .host(host!!.host)
          .build()
      val newRequest = request.newBuilder()
          .url(newUrl)
          .build()
      return chain.proceed(newRequest)
    }
    return chain.proceed(request)
  }
}