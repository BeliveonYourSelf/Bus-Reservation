package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("birthdate")
    val birthdate: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("token")
    val token: String
)