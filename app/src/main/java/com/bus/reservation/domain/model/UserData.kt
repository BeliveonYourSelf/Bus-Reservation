package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("user")
    val user: User
)