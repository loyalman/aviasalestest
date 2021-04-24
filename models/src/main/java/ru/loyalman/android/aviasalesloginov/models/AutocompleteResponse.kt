package ru.loyalman.android.aviasalesloginov.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AutocompleteResponse(
    @SerializedName("cities")
    val cities: List<CityDto> = emptyList()
)

@Parcelize
data class CityDto(
    @SerializedName("id")
    val cityId: Long,
    @SerializedName("city")
    val shortName: String,
    @SerializedName("fullname")
    val fullName: String,
    @SerializedName("location")
    val location: LocationDto,
) : Parcelable

@Parcelize
data class LocationDto(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
) : Parcelable
