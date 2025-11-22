package com.bus.reservation.domain.usecase

import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.ChangePasswordResponse
import com.bus.reservation.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import javax.inject.Inject


class ChangePasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(requestBody: RequestBody): Flow<Resource<ChangePasswordResponse>> {
        return flow<Resource<ChangePasswordResponse>> {
            emit(Resource.Loading())
            try {

                emit(Resource.Success(data = userRepository.changePassword(requestBody)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}