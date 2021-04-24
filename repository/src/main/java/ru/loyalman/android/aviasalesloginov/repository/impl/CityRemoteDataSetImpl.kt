package ru.loyalman.android.aviasalesloginov.repository.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.loyalman.android.aviasalesloginov.models.CityDto
import ru.loyalman.android.aviasalesloginov.repository.CityRemoteDataSet
import ru.loyalman.android.aviasalesloginov.repository.NetworkApi
import javax.inject.Inject

class CityRemoteDataSetImpl @Inject constructor(
    private val networkApi: NetworkApi
) : CityRemoteDataSet {
    override suspend fun searchCities(search: String): List<CityDto> = withContext(Dispatchers.IO) {
        networkApi.autoComplete(search).cities
    }
}