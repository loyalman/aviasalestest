package ru.loyalman.android.aviasalesloginov.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.loyalman.android.aviasalesloginov.repository.NetworkApi
import toothpick.config.Module

object ApiProviderDelegate {
    fun inject(parentModule: Module) = parentModule.run {

        bind(Gson::class.java)
            .toProvider(BaseGsonProvider::class.java)
            .providesSingleton()

        bind(OkHttpClient::class.java)
            .toProvider(OkHttpProvider::class.java)

        bind(Retrofit::class.java)
            .toProvider(BaseRetrofitProvider::class.java)
            .providesSingleton()

        bind(NetworkApi::class.java)
            .toProvider(ApiProvider::class.java)
            .providesSingleton()

        return@run
    }
}