package com.bus.reservation.presentation.bus_list

import androidx.paging.PagingData
import com.bus.reservation.domain.model.BusListData

data class AllBusListState(
    val isLoading: Boolean = false,
    val message: String? = "",
    val data: PagingData<BusListData>? = null
)
