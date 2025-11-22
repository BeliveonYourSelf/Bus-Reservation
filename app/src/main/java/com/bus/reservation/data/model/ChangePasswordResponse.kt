package com.bus.reservation.data.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success : Boolean,


)