package ru.loyalman.android.aviasalesloginov.base

import android.content.Context
import android.content.res.Resources
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import timber.log.Timber


val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun Context.showKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Context.hideKeyboard(windowToken: IBinder) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


inline fun <T> Fragment.observeDialogResult(
    navFragmentId: Int,
    requestCode: String,
    crossinline callback: (result: T) -> Unit
) {
    val navBackStackEntry = findNavController().getBackStackEntry(navFragmentId)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                requestCode
            )
        ) {
            val result = navBackStackEntry.savedStateHandle.get<T>(requestCode)
            result?.let {
                callback(it)
                navBackStackEntry.savedStateHandle.remove<T>(requestCode)
            }
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}

fun Fragment.showSimpleError(throwable: Throwable) {
    Timber.e(throwable)
    Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
}