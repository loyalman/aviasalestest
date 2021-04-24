package ru.loyalman.android.aviasalesloginov.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import toothpick.Toothpick
import toothpick.config.Module
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelInjectorFactory(
    private val modules: Array<Module> = arrayOf(),
    private val scopes: List<Any?> = emptyList()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        Toothpick.openScopes(DependenciesInjector.Scopes.APPLICATION, *scopes.toTypedArray())
            .installModules(*modules)
            .getInstance(modelClass) as T
}

inline fun <reified T : ViewModel> Fragment.injectViewModel(
    modules: Array<Module> = arrayOf(),
): T {
    val factory = ViewModelInjectorFactory(modules, listOf(this.activity, this))
    val provider = ViewModelProvider(this, factory)
    return provider.get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.injectSharedViewModel(
    modules: Array<Module> = arrayOf(),
): T {
    val factory = ViewModelInjectorFactory(modules, listOf(this.activity))
    val provider = ViewModelProvider(requireActivity(), factory)
    return provider.get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(
    modules: Array<Module> = arrayOf(),
): T {
    val factory = ViewModelInjectorFactory(modules, listOf(this))
    val provider = ViewModelProvider(this, factory)
    return provider.get(T::class.java)
}

inline fun <reified T : ViewModel> View.injectViewModel(
    modules: Array<Module> = arrayOf(),
): T {
    val owner = this.context as FragmentActivity
    val factory = ViewModelInjectorFactory(modules, listOf(owner))
    val provider = ViewModelProvider(owner, factory)
    return provider.get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(
    modules: Array<Module> = arrayOf(),
): ReadOnlyProperty<Fragment, T> = object : ReadOnlyProperty<Fragment, T> {

    private var viewModel: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T = viewModel
        ?: thisRef.injectViewModel<T>(modules)
            .also { vm -> viewModel = vm }
}

inline fun <reified T : ViewModel> Fragment.sharedViewModel(
    modules: Array<Module> = arrayOf(),
): ReadOnlyProperty<Fragment, T> = object : ReadOnlyProperty<Fragment, T> {

    private var viewModel: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T = viewModel
        ?: thisRef.injectSharedViewModel<T>(modules)
            .also { vm -> viewModel = vm }
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(
    modules: Array<Module> = arrayOf(),
): ReadOnlyProperty<FragmentActivity, T> = object : ReadOnlyProperty<FragmentActivity, T> {

    private var viewModel: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T = viewModel
        ?: thisRef.injectViewModel<T>(modules)
            .also { vm -> viewModel = vm }
}

inline fun <reified T : ViewModel> View.viewModel(
    modules: Array<Module> = arrayOf(),
): ReadOnlyProperty<View, T> = object : ReadOnlyProperty<View, T> {

    private var viewModel: T? = null

    override fun getValue(thisRef: View, property: KProperty<*>): T = viewModel
        ?: thisRef.injectViewModel<T>(modules)
            .also { vm -> viewModel = vm }
}
