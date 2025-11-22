package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("date")
    val date: String,
    @SerializedName("is_booked")
    val isBooked: Boolean,
    @SerializedName("seat_number")
    val seatNumber: Int
)