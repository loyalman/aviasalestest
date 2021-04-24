package ru.loyalman.android.aviasalesloginov.citypicker.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.loyalman.android.aviasalesloginov.citypicker.databinding.ItemCityBinding
import ru.loyalman.android.aviasalesloginov.models.CityDto

class CityViewHolder(
    private val binding: ItemCityBinding,
    private val onItemClicked: (item: CityDto) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CityDto) {
        binding.tvCity.text = item.fullName
        binding.tvCity.setOnClickListener {
            onItemClicked(item)
        }
    }
}