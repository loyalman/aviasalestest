package ru.loyalman.android.aviasalesloginov.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.loyalman.android.aviasalesloginov.BuildConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpProvider @Inject constructor() : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.d("Net: $message")
                    }
                }).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            addInterceptor(loggingInterceptor)
        }
        build()
    }
}
