package ru.loyalman.android.aviasalesloginov.citypicker

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.loyalman.android.aviasalesloginov.base.*
import ru.loyalman.android.aviasalesloginov.citypicker.adapters.CityAdapter
import ru.loyalman.android.aviasalesloginov.citypicker.databinding.DialogBottomCityPickerBinding
import ru.loyalman.android.aviasalesloginov.citypicker.di.SearchModule
import toothpick.config.Module
import javax.inject.Inject

class CityPickerDialog :
    BaseBottomSheetDialogFragment<DialogBottomCityPickerBinding, CityPickerEvent>(
        R.layout.dialog_bottom_city_picker, true
    ) {
    override val binding: DialogBottomCityPickerBinding by viewBinding(DialogBottomCityPickerBinding::bind)
    override val additionalModules: List<Module> = listOf(SearchModule())
    override val viewModel: CityPickerViewModel by viewModel()

    private val args: CityPickerDialogArgs by navArgs()

    private val adapter by lazy {
        CityAdapter(viewModel::onCityClicked)
    }

    @Inject
    lateinit var navigator: BaseNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etCitySearch.doOnLayout {
                it.requestFocus()
                context?.showKeyboard()
            }
            etCitySearch.doAfterTextChanged {
                viewModel.onSearchChanged(it.toString())
            }
            cityList.adapter = adapter
        }
        viewModel.cities.observe(viewLifecycleOwner, adapter::submitList)
    }

    override fun handleEvents(screenEvent: CityPickerEvent) = when (screenEvent) {
        is CityPickerEvent.Error -> {
            showSimpleError(screenEvent.throwable)
        }
        is CityPickerEvent.Picked -> {
            navigator.setResultToPrevFragment(args.requestCode, screenEvent.city)
            context?.hideKeyboard(binding.etCitySearch.windowToken)
            dismiss()
        }
    }

}