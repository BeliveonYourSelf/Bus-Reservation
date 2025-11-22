package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class UserTicketDetailsData(
    @SerializedName("bus")
    val bus: List<UserBusData>,
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("is_booked")
    val isBooked: Boolean,
    @SerializedName("price")
    val price: String,
    @SerializedName("seat_number")
    val seatNumber: Int,
    @SerializedName("user")
    val user: List<User>
)