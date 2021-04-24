package ru.loyalman.android.aviasalesloginov.citypicker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.loyalman.android.aviasalesloginov.citypicker.databinding.ItemCityBinding
import ru.loyalman.android.aviasalesloginov.models.CityDto

class CityAdapter(
    private val onItemClicked: (item: CityDto) -> Unit
) : ListAdapter<CityDto, CityViewHolder>(CityDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}