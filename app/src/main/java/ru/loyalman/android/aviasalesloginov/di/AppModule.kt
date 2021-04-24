package ru.loyalman.android.aviasalesloginov.di

import android.app.Application
import android.content.res.Resources
import ru.loyalman.android.aviasalesloginov.JetpackNavigationImpl
import ru.loyalman.android.aviasalesloginov.base.BaseNavigation
import toothpick.config.Module

class AppModule(application: Application) : Module() {
    init {
        bind(Resources::class.java).toInstance(application.resources)
        bind(BaseNavigation::class.java).to(JetpackNavigationImpl::class.java).singleton()

        ApiProviderDelegate.inject(this)
    }
}