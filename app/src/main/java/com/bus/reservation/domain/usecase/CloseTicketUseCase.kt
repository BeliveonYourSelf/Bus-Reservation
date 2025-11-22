package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.OpenTicketResponse
import com.bus.reservation.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CloseTicketUseCase(private val tickerRepository: TickerRepository) {

    operator fun invoke(): Flow<Resource<OpenTicketResponse>> {
        return flow<Resource<OpenTicketResponse>> {
            emit(Resource.Loading())

            try {
                emit(Resource.Success(data = tickerRepository.getAllCloseTicket()))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message))
            }

        }.flowOn(Dispatchers.IO)

    }

}