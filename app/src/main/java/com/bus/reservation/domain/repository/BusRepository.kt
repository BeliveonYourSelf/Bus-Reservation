package com.bus.reservation.domain.repository

import com.bus.reservation.data.model.BusListResponse
import com.bus.reservation.data.model.BusSearchResponse
import okhttp3.RequestBody

interface BusRepository {
    suspend fun getBusList(requestBody: RequestBody): BusListResponse
    suspend fun searchBus(requestBody: RequestBody): BusSearchResponse
}