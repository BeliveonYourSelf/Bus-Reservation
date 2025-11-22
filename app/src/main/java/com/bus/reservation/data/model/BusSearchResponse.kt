package com.bus.reservation.data.model


import com.bus.reservation.domain.model.BusSearchData
import com.google.gson.annotations.SerializedName

data class BusSearchResponse(
    @SerializedName("data")
    val data: List<BusSearchData>,
    @SerializedName("message")
    val message: String
)