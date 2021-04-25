package ru.loyalman.android.aviasalesloginov.featuresearchmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import ru.loyalman.android.aviasalesloginov.base.px
import ru.loyalman.android.aviasalesloginov.models.CityDto
import kotlin.math.abs


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

fun GoogleMap.drawBezierOnMap(
    latLng1: LatLng,
    latLng2: LatLng,
    @ColorInt color: Int,
): List<LatLng> {
    val factor = 0.5 //curve radius
    val longDiff = abs(latLng1.longitude - latLng2.longitude)
    val offsetPointsAngle = when {
        longDiff > 150 -> 5
        longDiff > 120 -> 10
        longDiff > 100 -> 30
        else -> 45
    }
    //угол между точками
    val heading = SphericalUtil.computeHeading(latLng1, latLng2)
    //рисстояние между точками
    val distanceBwPoints = SphericalUtil.computeDistanceBetween(latLng1, latLng2)
    val distanceOffsetPoints = distanceBwPoints * factor
    //находим еще 2 опорных точки
    val supportingDot2: LatLng
    val supportingDot3: LatLng
    val heading1and2: Double
    val heading4and3: Double
    if (heading > 0) {
        heading1and2 = fixHeading(heading - offsetPointsAngle)
        heading4and3 = fixHeading(heading - offsetPointsAngle + 180)
        supportingDot2 = SphericalUtil.computeOffset(latLng1, distanceOffsetPoints, heading1and2)
        supportingDot3 = SphericalUtil.computeOffset(latLng2, distanceOffsetPoints, heading4and3)
    } else {
        heading1and2 = fixHeading(heading + offsetPointsAngle)
        heading4and3 = fixHeading(heading + offsetPointsAngle - 180)
        supportingDot2 = SphericalUtil.computeOffset(latLng1, distanceOffsetPoints, heading1and2)
        supportingDot3 = SphericalUtil.computeOffset(latLng2, distanceOffsetPoints, heading4and3)
    }
    val distance2and3 = SphericalUtil.computeDistanceBetween(supportingDot2, supportingDot3)
    val heading2and3 = SphericalUtil.computeHeading(supportingDot2, supportingDot3)
    val polygon = PolylineOptions()
    val result = mutableListOf<LatLng>()
    val numberOfPoints = 600 //чем больше, тем плавнее
    val stepFactor = 1.0 / 600
    var currentFactor = 0.0
    for (i in 0..numberOfPoints) {
        //ищем точки на отрезках 1 порядка
        val dot1 =
            SphericalUtil.computeOffset(latLng1, distanceOffsetPoints * currentFactor, heading1and2)
        val dot2 =
            SphericalUtil.computeOffset(supportingDot2, distance2and3 * currentFactor, heading2and3)
        val dot3 = SphericalUtil.computeOffset(
            supportingDot3,
            distanceOffsetPoints * currentFactor,
            SphericalUtil.computeHeading(supportingDot3, latLng2)
        )
        //ищем точки на отрезках 2 порядка
        val dot21 = SphericalUtil.computeOffset(
            dot1,
            SphericalUtil.computeDistanceBetween(dot1, dot2) * currentFactor,
            SphericalUtil.computeHeading(dot1, dot2)
        )
        val dot22 = SphericalUtil.computeOffset(
            dot2,
            SphericalUtil.computeDistanceBetween(dot2, dot3) * currentFactor,
            SphericalUtil.computeHeading(dot2, dot3)
        )
        //ищем точку на отрезках 3 порядка
        val dot31 = SphericalUtil.computeOffset(
            dot21,
            SphericalUtil.computeDistanceBetween(dot21, dot22) * currentFactor,
            SphericalUtil.computeHeading(dot21, dot22)
        )
        polygon.add(dot31) //Adding in PolygonOptions
        result.add(dot31)
        currentFactor += stepFactor
    }
    polygon.color(color)
    polygon.width(7.px.toFloat())
    polygon.pattern(listOf(Dot(), Gap(30f))) //Skip if you want solid line
    this.addPolyline(polygon)
    return result
}

private fun GoogleMap.addDebugMarker(latLng: LatLng) {
    this.addMarker(
        MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker())
    )
}


fun getPointsCenter(from: LatLng, to: LatLng): LatLng {
    val heading = SphericalUtil.computeHeading(from, to)
    val distanceBwPoints = SphericalUtil.computeDistanceBetween(from, to)
//    return SphericalUtil.computeOffset(from, distanceBwPoints * 0.5, heading)
    return LatLng(
        (from.latitude + to.latitude) / 2,
        (from.longitude + to.longitude) / 2,
    )
}

private fun fixHeading(heading: Double): Double {
    if (heading > 180) return heading - 360
    if (heading < -180) return heading + 360
    return heading
}