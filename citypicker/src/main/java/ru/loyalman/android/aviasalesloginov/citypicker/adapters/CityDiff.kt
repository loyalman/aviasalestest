package ru.loyalman.android.aviasalesloginov.citypicker.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.loyalman.android.aviasalesloginov.models.CityDto

class CityDiff : DiffUtil.ItemCallback<CityDto>() {
    override fun areItemsTheSame(oldItem: CityDto, newItem: CityDto): Boolean {
        return oldItem.cityId == newItem.cityId
    }

    override fun areContentsTheSame(oldItem: CityDto, newItem: CityDto): Boolean {
        return oldItem == newItem
    }
}