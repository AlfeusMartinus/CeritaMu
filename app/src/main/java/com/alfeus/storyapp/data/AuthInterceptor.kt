package com.alfeus.storyapp.data

import com.alfeus.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userPreference: UserPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()

        val token = runBlocking {
            userPreference.getSession().first().token
        }

        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(requestHeaders)
    }
}

