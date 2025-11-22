package com.bus.reservation.domain.model

import java.io.Serializable

data class SeatModel(
    var isBooked: Boolean,
    var busId: String,
    val travellessname: String,
    val traveldate: String,
    val departure: String,
    val seatNo: String,
    val fare: String,
):Serializable
