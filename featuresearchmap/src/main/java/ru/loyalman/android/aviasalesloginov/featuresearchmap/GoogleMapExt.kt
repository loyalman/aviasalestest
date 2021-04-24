package ru.loyalman.android.aviasalesloginov.featuresearchmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import ru.loyalman.android.aviasalesloginov.base.px
import ru.loyalman.android.aviasalesloginov.models.CityDto


fun Context.getCityBitmap(cityName: String): Bitmap {
    val bitmap = IconGenerator(this).apply {
        setStyle(IconGenerator.STYLE_ORANGE)
        setBackground(
            ContextCompat.getDrawable(
                this@getCityBitmap,
                R.drawable.bg_city_marker
            )
        )
    }.makeIcon(cityName)
    return bitmap
}

fun GoogleMap.addCityMarker(city: CityDto, bitmap: Bitmap) {
    this.addMarker(MarkerOptions().apply {
        draggable(false)
        anchor(0.5F, 0.5F)
        icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        position(city.location.toLatLng())
        title(city.shortName)
    })
}

fun GoogleMap.drawCurveOnMap(
    latLng1: LatLng,
    latLng2: LatLng,
    isReversed: Boolean
): List<LatLng> {
    val factor = 0.5 //curve radius
    val heading = SphericalUtil.computeHeading(latLng1, latLng2)
    val distanceBwPoints = SphericalUtil.computeDistanceBetween(latLng1, latLng2)
    val pointInBetween: LatLng =
        SphericalUtil.computeOffset(latLng1, distanceBwPoints * 0.5, heading)

    //some math
    val radiusToFindCenter = (1 - factor * factor) * distanceBwPoints * 0.5 / (2 * factor)
    val imaginableCircleRadius = (1 + factor * factor) * distanceBwPoints * 0.5 / (2 * factor)

    val imaginableCircleCenter =
        SphericalUtil.computeOffset(pointInBetween, radiusToFindCenter, heading - 90.0)

    //Calculate heading between circle center and two points
    val headingToStart: Double
    val headingToFinish: Double
    if (isReversed) {
        headingToStart = SphericalUtil.computeHeading(imaginableCircleCenter, latLng2)
        headingToFinish = SphericalUtil.computeHeading(imaginableCircleCenter, latLng1)
    } else {
        headingToStart = SphericalUtil.computeHeading(imaginableCircleCenter, latLng1)
        headingToFinish = SphericalUtil.computeHeading(imaginableCircleCenter, latLng2)
    }

    //Calculate positions of points on circle border and add them to polyline options
    val numberOfPoints = 300 //more numberOfPoints more smooth curve you will get
    val step = when {
        headingToFinish - headingToStart > 180 -> {
            ((headingToFinish - headingToStart) - 360) / numberOfPoints
        }
        headingToFinish - headingToStart < -180 -> {
            -((-180 - headingToFinish) - (180 - headingToStart)) / numberOfPoints
        }
        else -> {
            (headingToFinish - headingToStart) / numberOfPoints
        }
    }
    //Create PolygonOptions object to draw on map
    val polygon = PolylineOptions()
    val result = mutableListOf<LatLng>()
    for (i in 0..numberOfPoints) {
        val point = SphericalUtil.computeOffset(
            imaginableCircleCenter,
            imaginableCircleRadius,
            fixHeading(headingToStart + i * step)
        )
        polygon.add(point) //Adding in PolygonOptions
        result.add(point)
    }
    polygon.color(Color.BLACK)
    polygon.width(5.px.toFloat())
    polygon.pattern(listOf(Dot(), Gap(30f))) //Skip if you want solid line
    this.addPolyline(polygon)
    return result
}

fun getPointsCenter(from: LatLng, to: LatLng): LatLng {
    val heading = SphericalUtil.computeHeading(from, to)
    val distanceBwPoints = SphericalUtil.computeDistanceBetween(from, to)
    return SphericalUtil.computeOffset(from, distanceBwPoints * 0.5, heading)
}

private fun fixHeading(heading: Double): Double {
    if (heading > 180) return heading - 360
    if (heading < -180) return heading + 360
    return heading
}