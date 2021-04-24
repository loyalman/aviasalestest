package ru.loyalman.android.aviasalesloginov.di

import retrofit2.Retrofit
import ru.loyalman.android.aviasalesloginov.repository.NetworkApi
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    private var retrofit: Retrofit
) : Provider<NetworkApi> {

    override fun get(): NetworkApi {
        return retrofit.create(NetworkApi::class.java)
    }
}