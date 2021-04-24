package ru.loyalman.android.aviasalesloginov.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import toothpick.config.Module
import kotlin.math.min

abstract class BaseBottomSheetDialogFragment<T : ViewBinding, S : BaseEvent>(
    @LayoutRes private val layout: Int,
    private val isFullscreen: Boolean = false
) : BottomSheetDialogFragment() {

    abstract val binding: T
    abstract val viewModel: BaseViewModel<S>
    open val additionalModules: List<Module> = emptyList()

    private val injector = DependenciesInjector()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val view = (it as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)
                ?: return@setOnShowListener

            val behavior = BottomSheetBehavior.from(view)

            val contentHeight = when (isFullscreen) {
                true -> ViewGroup.LayoutParams.MATCH_PARENT
                false -> ViewGroup.LayoutParams.WRAP_CONTENT
            }

            if (contentHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                behavior.peekHeight = 0
            }
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            val lp = view.layoutParams as CoordinatorLayout.LayoutParams
            lp.height = contentHeight
            lp.width = min(view.width, 480.px)
            lp.gravity = Gravity.START
            view.translationX = ((view.parent as ViewGroup).width - lp.width) / 2f
            view.layoutParams = lp
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout, container, false)
        viewModel.oneTimeEvents.observe(viewLifecycleOwner, ::handleEvents)
        return view
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


    override fun onDestroy() {
        injector.closeScope(this)
        super.onDestroy()
    }
}
