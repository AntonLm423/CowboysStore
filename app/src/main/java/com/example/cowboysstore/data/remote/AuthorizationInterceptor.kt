package com.example.cowboysstore.data.remote

import com.example.cowboysstore.data.local.prefs.Preferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val AUTHORIZATION_HEADER = "Authorization"

class AuthorizationInterceptor @Inject constructor(
    private val preferences: Preferences
    ) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                AUTHORIZATION_HEADER,
                "Bearer ${preferences.accessToken}"
            )
            .build()
        return chain.proceed(request)
    }
}