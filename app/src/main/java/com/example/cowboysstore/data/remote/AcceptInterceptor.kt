package com.example.cowboysstore.data.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val ACCEPT_HEADER = "accept"

class AcceptInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                ACCEPT_HEADER,
                "/"
            )
            .build()
        return chain.proceed(request)
    }
}