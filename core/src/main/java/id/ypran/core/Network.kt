package id.ypran.core

import id.ypran.core.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createNetworkClient(baseUrl: String) = retrofitClient(baseUrl)

private fun retrofitClient(baseUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(
        OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = BASIC
            }).build()
    )
    .addCallAdapterFactory(LiveDataCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
