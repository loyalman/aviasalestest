package ru.loyalman.android.aviasalesloginov.base

import toothpick.Toothpick
import toothpick.config.Module

class DependenciesInjector {

    fun <V> openScope(
        target: V,
        scopes: List<Any>,
        closeFinally: Boolean = false,
        additionalModules: List<Module> = emptyList()
    ) {
        Toothpick.openScopes(Scopes.APPLICATION, *scopes.toTypedArray())
            .installModules(*additionalModules.toTypedArray())
            .let { scope -> Toothpick.inject(target, scope) }
            .also { if (closeFinally) scopes.forEach(Toothpick::closeScope) }
    }

    fun <V> closeScope(scope: V) = Toothpick.closeScope(scope)

    enum class Scopes {
        APPLICATION
    }
}
