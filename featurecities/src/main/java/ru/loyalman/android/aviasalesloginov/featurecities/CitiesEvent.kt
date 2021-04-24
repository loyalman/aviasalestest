package ru.loyalman.android.aviasalesloginov.featurecities

import ru.loyalman.android.aviasalesloginov.base.BaseEvent
import ru.loyalman.android.aviasalesloginov.models.CityDto

sealed class CitiesEvent : BaseEvent {
    data class Error(val throwable: Throwable) : CitiesEvent()
    data class GoToSearchingMap(val cityFrom: CityDto, val cityTo: CityDto) : CitiesEvent()
}