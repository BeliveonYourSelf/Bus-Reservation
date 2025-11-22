package com.bus.reservation.data.model


import com.bus.reservation.domain.model.BusListData
import com.google.gson.annotations.SerializedName

data class BusListResponse(
    @SerializedName("data")
    val data: List<BusListData>,
    @SerializedName("message")
    val message: String
)