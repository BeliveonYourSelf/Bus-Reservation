package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.BookSeatResponse
import com.bus.reservation.data.model.OpenTicketResponse
import com.bus.reservation.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class RegisterTickerUseCase(val tickerRepository: TickerRepository) {
    operator fun invoke(requestBody: RequestBody): Flow<Resource<BookSeatResponse>> {
        return flow<Resource<BookSeatResponse>> {
            emit(Resource.Loading())

            try {
                emit(Resource.Success(data = tickerRepository.registerTicket(requestBody)))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}