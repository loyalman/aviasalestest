package ru.loyalman.android.aviasalesloginov.citypicker.di

import ru.loyalman.android.aviasalesloginov.repository.CityRemoteDataSet
import ru.loyalman.android.aviasalesloginov.repository.CityRepo
import ru.loyalman.android.aviasalesloginov.repository.impl.CityRemoteDataSetImpl
import ru.loyalman.android.aviasalesloginov.repository.impl.CityRepoImpl
import toothpick.config.Module

class SearchModule : Module() {
    init {
        bind(CityRemoteDataSet::class.java).to(CityRemoteDataSetImpl::class.java)
        bind(CityRepo::class.java).to(CityRepoImpl::class.java)
    }
}