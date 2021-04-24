package ru.loyalman.android.aviasalesloginov.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import toothpick.config.Module

abstract class BaseActivity : AppCompatActivity() {

    private val injector = DependenciesInjector()
    open val additionalModules: List<Module> = emptyList()

    override fun attachBaseContext(newBase: Context?) {
        injector.openScope(
            target = this,
            scopes = listOf(this),
            additionalModules = this.additionalModules
        )
        super.attachBaseContext(newBase)

    }

    override fun onDestroy() {
        injector.closeScope(this)
        super.onDestroy()
    }
}