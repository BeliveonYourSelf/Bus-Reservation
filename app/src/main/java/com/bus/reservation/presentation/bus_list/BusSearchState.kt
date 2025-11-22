package com.bus.reservation.presentation.bus_list

import com.bus.reservation.domain.model.BusSearchData
import java.io.Serializable

data class BusSearchState(
    val isLoading: Boolean = false,
    val data: List<BusSearchData>?=null,
    val message: String? = ""
) : Serializable
