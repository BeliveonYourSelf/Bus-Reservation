package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BusSearchData (
    @SerializedName("availability")
    val availability: Int,
    @SerializedName("departure")
    val departure: String,
    @SerializedName("fare")
    val fare: Int,
    @SerializedName("from")
    val from: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("traveldate")
    val traveldate: String,
    @SerializedName("travelsname")
    val travelsname: String,
    @SerializedName("__v")
    val v: Int
) : Serializable