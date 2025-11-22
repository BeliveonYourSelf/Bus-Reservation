package com.bus.reservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bus.reservation.R
import com.bus.reservation.databinding.ItemSeatsBinding
import com.bus.reservation.domain.model.OpenTicketData
import com.bus.reservation.domain.model.SeatModel

class TicketAdapter(val context: Context, val dataList: List<SeatModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var onSeatClicker : ((SeatModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyTickerHolder(ItemSeatsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
        return 20
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mytickerholder = holder as MyTickerHolder
        val items = dataList[position]
        mytickerholder.binding.tvSeats.text = items.seatNo
        if (items.isBooked) {
            mytickerholder.binding.tvSeats.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )
        } else {
            mytickerholder.binding.tvSeats.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.avilable
                )
            )
        }
        mytickerholder.itemView.setOnClickListener {
            onSeatClicker!!.invoke(items)
        }
//        mytickerholder.bind(items)
    }

    class MyTickerHolder(val binding: ItemSeatsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(openTicketData: OpenTicketData) {

        }
    }
}


