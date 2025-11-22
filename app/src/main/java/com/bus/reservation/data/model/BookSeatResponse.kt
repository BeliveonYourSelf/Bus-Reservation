package com.bus.reservation.data.model


import com.google.gson.annotations.SerializedName

data class BookSeatResponse(
    @SerializedName("data")
    val data: BookSearData,
    @SerializedName("message")
    val message: String
)