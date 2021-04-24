package ru.loyalman.android.aviasalesloginov.featurecities

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.loyalman.android.aviasalesloginov.base.*
import ru.loyalman.android.aviasalesloginov.featurecities.databinding.MainFragmentBinding
import ru.loyalman.android.aviasalesloginov.models.CityDto
import javax.inject.Inject

class MainFragment : BaseFragment<MainFragmentBinding, CitiesEvent>() {

    override val layoutId: Int = R.layout.main_fragment
    override val binding: MainFragmentBinding by viewBinding(MainFragmentBinding::bind)
    override val viewModel: MainViewModel by viewModel()

    @Inject
    lateinit var navigator: BaseNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSearch.setOnClickListener {
                viewModel.onSearchClicked()
            }
            etCityFrom.setOnClickListener {
                navigator.navigateTo(
                    R.id.nav_cities_to_city_picker,
                    bundleOf(getString(R.string.argument_requestCode) to RC_CITY_FROM)
                )
            }
            etCityTo.setOnClickListener {
                navigator.navigateTo(
                    R.id.nav_cities_to_city_picker,
                    bundleOf(getString(R.string.argument_requestCode) to RC_CITY_TO)
                )
            }
        }

        observeDialogResult<CityDto>(R.id.mainFragment, RC_CITY_FROM) {
            viewModel.onCityFromPicked(it)
        }
        observeDialogResult<CityDto>(R.id.mainFragment, RC_CITY_TO) {
            viewModel.onCityToPicked(it)
        }
        viewModel.cityFromText.observe(viewLifecycleOwner, {
            binding.etCityFrom.text = it
        })
        viewModel.cityToText.observe(viewLifecycleOwner, {
            binding.etCityTo.text = it
        })
        viewModel.searchEnabled.observe(viewLifecycleOwner, {
            binding.btnSearch.isEnabled = it
        })
    }

    override fun handleEvents(screenEvent: CitiesEvent) = when (screenEvent) {
        is CitiesEvent.Error -> {
            showSimpleError(screenEvent.throwable)
        }
        is CitiesEvent.GoToSearchingMap -> {
            navigator.navigateTo(
                R.id.nav_cities_to_map,
                bundleOf(
                    getString(R.string.argument_cityFrom) to screenEvent.cityFrom,
                    getString(R.string.argument_cityTo) to screenEvent.cityTo
                )
            )
        }
    }


    companion object {
        private const val RC_CITY_FROM = "CITY_FROM"
        private const val RC_CITY_TO = "CITY_TO"
    }
}