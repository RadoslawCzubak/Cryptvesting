package com.rczubak.cryptvesting.data.network

import com.rczubak.cryptvesting.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.CRYPTOCURRENCIES_KEY)
            .build()
        val requestBuilder = originalRequest.newBuilder()
            .url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}