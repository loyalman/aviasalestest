package ru.loyalman.android.aviasalesloginov.repository.impl

import ru.loyalman.android.aviasalesloginov.models.CityDto
import ru.loyalman.android.aviasalesloginov.repository.CityRemoteDataSet
import ru.loyalman.android.aviasalesloginov.repository.CityRepo
import javax.inject.Inject

class CityRepoImpl @Inject constructor(
    private val cityRemoteDataSet: CityRemoteDataSet,
) : CityRepo {
    override suspend fun searchCities(search: String): List<CityDto> {
        return cityRemoteDataSet.searchCities(search)
    }
}