package com.bus.reservation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bus.reservation.databinding.ItemAllTicketsBinding
import com.bus.reservation.domain.model.OpenTicketData
import com.bus.reservation.domain.model.UserTicketDetailsData
import javax.inject.Inject

class AllTicketAdapter (val context: Context, val dataList: List<UserTicketDetailsData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onSeatClicker: ((UserTicketDetailsData) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyTickerHolder(
            ItemAllTicketsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
        return 20
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mytickerholder = holder as MyTickerHolder
        val items = dataList[position]

        with(mytickerholder.binding) {
            tvTravelesValue.text = dataList[position].bus[0].travelsname
            tvSeatValue.text = dataList[position].seatNumber.toString()
            tvPriceValue.text = dataList[position].price
            tvFromValue.text = dataList[position].bus[0].from
            tvToValue.text = dataList[position].bus[0].to
            tvDateValue.text = java.time.ZonedDateTime.parse(dataList[0].date).toLocalDate().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")
            )
            tvTimeValue.text = "9:00:00 PM"
            tvNameValues.text = dataList[position].user[0].firstName + " " + dataList[position].user[0].lastName
            tvPhoneValue.text = dataList[position].user[0].phone
            tvEmailValue.text = dataList[position].user[0].email
        }

//        mytickerholder.itemView.setOnClickListener {
//            onSeatClicker!!.invoke(items)
//        }
    }

    class MyTickerHolder(val binding: ItemAllTicketsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}


