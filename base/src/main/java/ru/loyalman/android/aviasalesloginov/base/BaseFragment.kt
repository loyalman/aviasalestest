package ru.loyalman.android.aviasalesloginov.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import toothpick.config.Module

abstract class BaseFragment<T : ViewBinding, S : BaseEvent> : Fragment() {
    abstract val viewModel: BaseViewModel<S>
    abstract val layoutId: Int

    private var _binding: T? = null
    open val binding: T get() = _binding!!

    open val additionalModules: List<Module> = emptyList()

    private val injector = DependenciesInjector()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.oneTimeEvents.observe(viewLifecycleOwner, ::handleEvents)
    }

    abstract fun handleEvents(screenEvent: S)

    override fun onAttach(context: Context) {
        injector.openScope(
            target = this,
            scopes = listOf(context, this),
            additionalModules = this.additionalModules
        )
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        injector.closeScope(this)
        super.onDestroy()
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
        }

    fun launchCoroutine(whatToDo: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(
            context = exceptionHandler,
            block = whatToDo
        )
    }
}