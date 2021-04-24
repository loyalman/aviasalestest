package ru.loyalman.android.aviasalesloginov.citypicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import ru.loyalman.android.aviasalesloginov.base.BaseViewModel
import ru.loyalman.android.aviasalesloginov.models.CityDto
import ru.loyalman.android.aviasalesloginov.repository.CityRepo
import javax.inject.Inject

class CityPickerViewModel @Inject constructor(
    private val cityRepo: CityRepo,
) : BaseViewModel<CityPickerEvent>() {
    override val errorHandler: (Throwable) -> Unit = {
        _oneTimeEvents.postValue(CityPickerEvent.Error(it))
    }

    private val searchFlow: MutableSharedFlow<String> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _cities: MutableLiveData<List<CityDto>> = MutableLiveData(emptyList())
    val cities: LiveData<List<CityDto>>
        get() {
            subscribeSum()
            return _cities
        }


    fun onSearchChanged(search: String) = launchCatching {
        searchFlow.emit(search)
    }

    private fun subscribeSum() = launchCatching {
        searchFlow.debounce(300).collect {
            val cities = cityRepo.searchCities(it)
            _cities.postValue(cities)
        }
    }

    fun onCityClicked(city: CityDto) {
        _oneTimeEvents.postValue(CityPickerEvent.Picked(city))
    }
}