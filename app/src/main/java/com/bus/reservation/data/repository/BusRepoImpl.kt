package com.bus.reservation.data.repository

import com.bus.reservation.data.model.BusListResponse
import com.bus.reservation.data.model.BusSearchResponse
import com.bus.reservation.data.network.BusApiService
import com.bus.reservation.domain.repository.BusRepository
import okhttp3.RequestBody

class BusRepoImpl(private val busApiService: BusApiService) : BusRepository {
    override suspend fun getBusList(requestBody: RequestBody): BusListResponse {
        return busApiService.getBusList(requestBody)
    }

    override suspend fun searchBus(requestBody: RequestBody): BusSearchResponse {
        return busApiService.searchBus(requestBody)
    }
}