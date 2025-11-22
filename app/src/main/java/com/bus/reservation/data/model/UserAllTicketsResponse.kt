package com.bus.reservation.data.model


import com.bus.reservation.domain.model.UserTicketDetailsData
import com.google.gson.annotations.SerializedName

data class UserAllTicketsResponse(
    @SerializedName("data")
    val data: List<UserTicketDetailsData>,
    @SerializedName("message")
    val message: String
)