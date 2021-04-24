package ru.loyalman.android.aviasalesloginov.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject
import javax.inject.Provider

class BaseGsonProvider @Inject constructor() : Provider<Gson> {
    override fun get(): Gson =
        GsonBuilder()
            .setPrettyPrinting()
            .create()
}
