package ru.loyalman.android.aviasalesloginov.featuresearchmap.ui

import ru.loyalman.android.aviasalesloginov.base.BaseEvent

sealed class SearchingMapEvent : BaseEvent {
    data class Error(val throwable: Throwable) : SearchingMapEvent()
}