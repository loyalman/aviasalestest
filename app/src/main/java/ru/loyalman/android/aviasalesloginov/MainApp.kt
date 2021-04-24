package ru.loyalman.android.aviasalesloginov

import android.app.Application
import ru.loyalman.android.aviasalesloginov.base.DependenciesInjector
import ru.loyalman.android.aviasalesloginov.di.AppModule
import timber.log.Timber
import toothpick.Toothpick

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogging()
    }

    private fun initDI() {
        Toothpick.openScope(DependenciesInjector.Scopes.APPLICATION)
            .apply {
                installModules(
                    AppModule(this@MainApp),
                )
            }
            .also { scope -> scope.inject(this@MainApp) }
        Unit
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}