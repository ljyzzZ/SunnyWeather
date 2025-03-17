package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.databinding.PlaceItemBinding
import com.sunnyweather.android.logic.model.Place

/**
 * RecyclerView 适配器类
 */
class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: PlaceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val placeName: TextView = binding.placeName
        val placeAddress: TextView = binding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
}