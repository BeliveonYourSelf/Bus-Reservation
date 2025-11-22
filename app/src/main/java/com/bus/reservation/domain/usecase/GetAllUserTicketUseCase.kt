package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class GetAllUserTicketUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<Resource<UserAllTicketsResponse>> {

        return flow {
            emit(Resource.Loading())

            try {
                emit(Resource.Success(data = userRepository.getUserAllTickets()))

            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }
}