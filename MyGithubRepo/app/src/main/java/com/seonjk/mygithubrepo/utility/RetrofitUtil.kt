package com.seonjk.mygithubrepo.utility

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.seonjk.mygithubrepo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Url {
    const val GITHUB_URL = "http://github.com"
    const val GITHUB_API_URL = "http://api.github.com"
}

object RetrofitUtil {

    val authApiService: AuthApiService by lazy { getGihubAuthRetrofit().create(AuthApiService::class.java) }

    private fun getGihubAuthRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Url.GITHUB_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            )
        )
        .client(buildOkHttpClient())
        .build()

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }
}