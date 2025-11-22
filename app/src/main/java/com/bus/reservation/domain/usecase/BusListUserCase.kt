package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.BusListResponse
import com.bus.reservation.domain.repository.BusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class BusListUserCase(private val busRepository: BusRepository) {
    operator fun invoke(requestBody: RequestBody): Flow<Resource<BusListResponse>> {

        return flow {
            emit(Resource.Loading())
            try {
                emit((Resource.Success(data = busRepository.getBusList(requestBody))))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}