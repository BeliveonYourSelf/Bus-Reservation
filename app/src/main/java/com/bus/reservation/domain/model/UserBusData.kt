package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class UserBusData(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("travelsname")
    val travelsname: String
)