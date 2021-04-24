package ru.loyalman.android.aviasalesloginov.repository

import ru.loyalman.android.aviasalesloginov.models.CityDto

interface CityRepo {
    suspend fun searchCities(search: String): List<CityDto>
}