package com.bus.reservation.presentation.bus_list

import com.bus.reservation.domain.model.BusListData

data class BusListState(
    val isLoading: Boolean = false,
    val message: String? = "",
    val data: List<BusListData>? = null
)
