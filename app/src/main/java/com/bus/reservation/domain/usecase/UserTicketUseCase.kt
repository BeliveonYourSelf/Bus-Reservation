package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.data.model.UserTicketDetailsResponse
import com.bus.reservation.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class UserTicketUseCase(private val userRepository: UserRepository) {
    operator fun invoke(requestBody: RequestBody): Flow<Resource<UserTicketDetailsResponse>> {

        return flow {
            emit(Resource.Loading())

            try {
                emit(Resource.Success(data = userRepository.getUserTicketDetails(requestBody)))

            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }
}