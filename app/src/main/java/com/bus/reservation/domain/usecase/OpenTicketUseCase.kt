package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.OpenTicketResponse
import com.bus.reservation.domain.repository.TickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OpenTicketUseCase(private val openRepository: TickerRepository) {
    operator fun invoke(): Flow<Resource<OpenTicketResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(data = openRepository.getAllOpenTicket()))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }
    }
}