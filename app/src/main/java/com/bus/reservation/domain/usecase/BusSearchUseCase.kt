package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.BusSearchResponse
import com.bus.reservation.domain.repository.BusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class BusSearchUseCase(private val busRepository: BusRepository) {
    operator fun invoke(requestBody: RequestBody): Flow<Resource<BusSearchResponse>> {
        return flow {
            emit((Resource.Loading()))

            try {
                emit((Resource.Success(data = busRepository.searchBus(requestBody))))
            } catch (e: Exception) {
                emit((Resource.Error(e.message)))
            }
        }
    }