package ru.loyalman.android.aviasalesloginov.featuresearchmap

import com.google.android.gms.maps.model.LatLng
import ru.loyalman.android.aviasalesloginov.models.LocationDto

fun LocationDto.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}