package ru.loyalman.android.aviasalesloginov.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<T : BaseEvent> : ViewModel() {


    private val viewModelErrorHandler =
        CoroutineExceptionHandler { _, throwable ->
            errorHandler(throwable)
        }

    abstract val errorHandler: (Throwable) -> Unit

    fun <T> launchCatching(block: suspend () -> T) {
        viewModelScope.launch(viewModelErrorHandler) { block() }
    }
    val oneTimeEvents: LiveData<T>
        get() = _oneTimeEvents
    protected val _oneTimeEvents = SingleLiveEvent<T>()

}