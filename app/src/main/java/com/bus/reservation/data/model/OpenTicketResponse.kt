package com.bus.reservation.data.model


import com.bus.reservation.domain.model.OpenTicketData
import com.google.gson.annotations.SerializedName

data class OpenTicketResponse(
    @SerializedName("data")
    val data: List<OpenTicketData>,
    @SerializedName("message")
    val message: String
)