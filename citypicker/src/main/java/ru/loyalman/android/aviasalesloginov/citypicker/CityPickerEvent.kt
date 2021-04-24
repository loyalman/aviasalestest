package ru.loyalman.android.aviasalesloginov.citypicker

import ru.loyalman.android.aviasalesloginov.base.BaseEvent
import ru.loyalman.android.aviasalesloginov.models.CityDto

sealed class CityPickerEvent : BaseEvent {
    data class Error(val throwable: Throwable) : CityPickerEvent()
    data class Picked(val city: CityDto) : CityPickerEvent()
}
