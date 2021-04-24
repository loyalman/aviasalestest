package ru.loyalman.android.aviasalesloginov.featuresearchmap.ui

import ru.loyalman.android.aviasalesloginov.base.BaseViewModel
import ru.loyalman.android.aviasalesloginov.models.CityDto
import javax.inject.Inject

class SearchingMapViewModel @Inject constructor() : BaseViewModel<SearchingMapEvent>() {
    override val errorHandler: (Throwable) -> Unit = {
        _oneTimeEvents.postValue(SearchingMapEvent.Error(it))
    }

    fun initMap(cityFrom: CityDto, cityTo: CityDto) {

    }
}