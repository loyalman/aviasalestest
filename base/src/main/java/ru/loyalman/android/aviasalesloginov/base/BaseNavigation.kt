package ru.loyalman.android.aviasalesloginov.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

interface BaseNavigation {
    fun navigateTo(
        destination: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    )
    fun navigateTo(direction:NavDirections)

    fun navigateToDeepLink(url: String)
    fun back()
    fun onNavigateUp(): Boolean
    fun bindController(navController: NavController)
    fun unbindController()
    fun <T> setResultToPrevFragment(key: String, value: T)
    fun <T> trackResult(key: String): LiveData<T>
}
