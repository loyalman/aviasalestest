package ru.loyalman.android.aviasalesloginov.featuresearchmap.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.*
import ru.loyalman.android.aviasalesloginov.base.BaseFragment
import ru.loyalman.android.aviasalesloginov.base.px
import ru.loyalman.android.aviasalesloginov.base.showSimpleError
import ru.loyalman.android.aviasalesloginov.base.viewModel
import ru.loyalman.android.aviasalesloginov.featuresearchmap.*
import ru.loyalman.android.aviasalesloginov.featuresearchmap.databinding.FragmentMapBinding
import ru.loyalman.android.aviasalesloginov.featuresearchmap.toLatLng
import java.util.*


class SearchingMapFragment : BaseFragment<FragmentMapBinding, SearchingMapEvent>(),
    OnMapReadyCallback {
    override val viewModel: SearchingMapViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_map
    override val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)
    private var map: GoogleMap? = null
    private val pls: MutableList<LatLng> = mutableListOf()

    private val args: SearchingMapFragmentArgs by navArgs()

    override fun handleEvents(screenEvent: SearchingMapEvent) = when (screenEvent) {
        is SearchingMapEvent.Error -> {
            showSimpleError(screenEvent.throwable)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@SearchingMapFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(mapNullable: GoogleMap?) {
        mapNullable?.let { googleMap ->
            googleMap.uiSettings.setAllGesturesEnabled(false)
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isScrollGesturesEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true
            map = googleMap
            val latLonFrom = args.cityFrom.location.toLatLng()
            val latLonTo = args.cityTo.location.toLatLng()

            viewModel.initMap(args.cityFrom, args.cityTo)

            val bounds = LatLngBounds.builder()
                .include(latLonFrom)
                .include(latLonTo).build()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 48.px))

            googleMap.addCityMarker(
                args.cityFrom,
                requireContext().getCityBitmap(args.cityFrom.shortName)
            )
            googleMap.addCityMarker(
                args.cityTo,
                requireContext().getCityBitmap(args.cityTo.shortName)
            )

            val center = getPointsCenter(latLonFrom, latLonTo)
            pls.addAll(googleMap.drawCurveOnMap(latLonFrom, center, false))
            pls.addAll(googleMap.drawCurveOnMap(latLonTo, center, true))
            handlePlane(googleMap, latLonFrom)
        }
    }

    private fun handlePlane(googleMap: GoogleMap, startPosition: LatLng) {
        val plane = googleMap.addMarker(
            MarkerOptions().apply {
                draggable(false)
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                anchor(0.5F, 0.5F)
                position(startPosition)
            }
        )
        val frameDelay =
            (1000F / (requireActivity().windowManager.defaultDisplay?.refreshRate ?: 60F)).toLong()
        lifecycleScope.launch(Dispatchers.Main) {
            val size = pls.size
            var index = 0
            while (isActive) {
                delay(frameDelay)
                val heading = SphericalUtil.computeHeading(pls[index], pls[index + 1]) - 90
                plane.rotation = heading.toFloat()
                plane.position = pls[index]
                index++
                if (index == size - 1) index = 0
            }
        }

    }


    //law is law
    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onStart() {
        binding.mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}