package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OpenTicketData(
    @SerializedName("bus")
    val bus: String,
    @SerializedName("CreatedAt")
    val createdAt: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("is_booked")
    val isBooked: Boolean,
    @SerializedName("passenger")
    val passenger: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("seat_number")
    val seatNumber: Int,
    @SerializedName("__v")
    val v: Int
): Serializable