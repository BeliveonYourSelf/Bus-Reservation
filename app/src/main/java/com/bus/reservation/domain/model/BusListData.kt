package com.bus.reservation.domain.model


import com.google.gson.annotations.SerializedName

data class BusListData(
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
    @SerializedName("tickets")
    val tickets: List<Ticket>,
    @SerializedName("to")
    val to: String,
    @SerializedName("traveldate")
    val traveldate: String,
    @SerializedName("travelsname")
    val travelsname: String
)