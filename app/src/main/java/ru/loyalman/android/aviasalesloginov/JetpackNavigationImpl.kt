package ru.loyalman.android.aviasalesloginov

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import ru.loyalman.android.aviasalesloginov.base.BaseNavigation
import javax.inject.Inject

class JetpackNavigationImpl @Inject constructor() :
    BaseNavigation {
    private var navController: NavController? = null

    override fun navigateTo(
        destination: Int,
        args: Bundle?,
        navOptions: NavOptions?,
        extras: Navigator.Extras?
    ) {
        navController?.navigate(destination, args, navOptions, extras)
    }

    override fun navigateTo(direction: NavDirections) {
        navController?.navigate(direction)
    }

    override fun navigateToDeepLink(url: String) {
        navController?.navigate(Uri.parse(url))
    }

    override fun back() {
        navController?.popBackStack()
    }

    override fun onNavigateUp(): Boolean = navController?.navigateUp() ?: false

    override fun bindController(navController: NavController) {
        this.navController = navController
    }

    override fun unbindController() {
        navController = null
    }

    override fun <T> setResultToPrevFragment(key: String, value: T) {
        navController?.previousBackStackEntry?.savedStateHandle?.set(key, value)
    }

    override fun <T> trackResult(key: String): LiveData<T> {
        return navController?.currentBackStackEntry?.savedStateHandle?.getLiveData(key) ?: (object :
            LiveData<T>() {})
    }
}