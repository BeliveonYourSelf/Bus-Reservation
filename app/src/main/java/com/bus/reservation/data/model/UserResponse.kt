package com.bus.reservation.data.model


import com.bus.reservation.domain.model.UserData
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data")
    val data: UserData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)