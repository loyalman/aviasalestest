package ru.loyalman.android.aviasalesloginov.featurecities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.loyalman.android.aviasalesloginov.base.BaseViewModel
import ru.loyalman.android.aviasalesloginov.models.CityDto
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel<CitiesEvent>() {

    override val errorHandler: (Throwable) -> Unit = {
        _oneTimeEvents.postValue(CitiesEvent.Error(it))
    }

    private var cityFrom: CityDto? = null
    private var cityTo: CityDto? = null

    private val _cityFromText: MutableLiveData<String> = MutableLiveData("")
    val cityFromText: LiveData<String> = _cityFromText

    private val _cityToText: MutableLiveData<String> = MutableLiveData("")
    val cityToText: LiveData<String> = _cityToText

    private val _searchEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val searchEnabled: LiveData<Boolean> = _searchEnabled

    fun onSearchClicked() {
        val from = cityFrom
        val to = cityTo
        if (from != null && to != null) {
            _oneTimeEvents.postValue(CitiesEvent.GoToSearchingMap(from, to))
        }
    }

    fun onCityFromPicked(city: CityDto) {
        cityFrom = city
        _cityFromText.postValue(city.fullName)
        fixSearchEnabled()
    }

    fun onCityToPicked(city: CityDto) {
        cityTo = city
        _cityToText.postValue(city.fullName)
        fixSearchEnabled()
    }

    private fun fixSearchEnabled() {
        _searchEnabled.postValue(cityFrom != null && cityTo != null)
    }

}