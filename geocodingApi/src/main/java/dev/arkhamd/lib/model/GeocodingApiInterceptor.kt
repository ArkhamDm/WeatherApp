package dev.arkhamd.lib.model

import okhttp3.Interceptor
import okhttp3.Response

internal class GeocodingApiInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", apiKey).build()
        val request = chain.request().newBuilder().url(url).build()

        return chain.proceed(request)
    }
}