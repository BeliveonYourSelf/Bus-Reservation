package com.bus.reservation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bus.reservation.databinding.ItemBusListBinding
import com.bus.reservation.domain.model.BusListData

class BuslistPagingAdapter : PagingDataAdapter<BusListData, BuslistPagingAdapter.MyListHolder>(COMAPRATOR) {

    override fun onBindViewHolder(holder: BuslistPagingAdapter.MyListHolder, position: Int) {
        holder.bind(getItem(position)!!, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BuslistPagingAdapter.MyListHolder {
        return MyListHolder(
            ItemBusListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyListHolder(val binding: ItemBusListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(busSearchData: BusListData, position: Int) {
            binding.tvIndex.text = "" + (position + 1)
            binding.tvBusName.text = busSearchData.travelsname
            binding.tvFromValue.text = busSearchData.from
            binding.tvToValue.text = busSearchData.to
            binding.tvTime.text = busSearchData.departure
            binding.tvDate.text = busSearchData.traveldate

        }
    }

    companion object {
        private val COMAPRATOR = object : DiffUtil.ItemCallback<BusListData>() {
            override fun areItemsTheSame(oldItem: BusListData, newItem: BusListData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BusListData, newItem: BusListData): Boolean {
                return oldItem == newItem
            }

        }

    }
}