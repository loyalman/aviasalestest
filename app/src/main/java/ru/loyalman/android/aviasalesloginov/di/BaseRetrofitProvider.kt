package ru.loyalman.android.aviasalesloginov.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class BaseRetrofitProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
) : Provider<Retrofit> {
    override fun get(): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://yasen.hotellook.com/")
            .build()
}
