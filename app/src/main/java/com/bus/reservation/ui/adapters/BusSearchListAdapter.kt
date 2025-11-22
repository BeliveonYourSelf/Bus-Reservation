package com.bus.reservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bus.reservation.databinding.ItemBusListBinding
import com.bus.reservation.domain.model.BusSearchData

class BusSearchListAdapter(val context: Context, val datalist: List<BusSearchData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onBusClick: ((BusSearchData) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyListHolder(ItemBusListBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myListHolder = holder as MyListHolder
        myListHolder.bind(datalist[position], position)
        myListHolder.itemView.setOnClickListener {
            onBusClick?.invoke(datalist[position])
        }

    }

    inner class MyListHolder(val binding: ItemBusListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(busSearchData: BusSearchData, position: Int) {
            binding.tvIndex.text = "" + (position + 1)
            binding.tvBusName.text = busSearchData.travelsname
            binding.tvFromValue.text = busSearchData.from
            binding.tvToValue.text = busSearchData.to
            binding.tvTime.text = busSearchData.departure
            binding.tvDate.text = busSearchData.traveldate

        }
    }
}