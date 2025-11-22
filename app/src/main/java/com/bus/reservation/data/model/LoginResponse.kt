package com.bus.reservation.data.model


import com.bus.reservation.domain.model.LoginData
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)