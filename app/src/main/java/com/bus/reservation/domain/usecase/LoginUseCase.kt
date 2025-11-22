package com.bus.reservation.domain.usecase

import android.util.Log
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.LoginResponse
import com.bus.reservation.domain.model.LoginUser
import com.bus.reservation.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginUseCase(private val loginRepository: UserRepository) {
    operator fun invoke(user:LoginUser) : Flow<Resource<LoginResponse>> {
        return flow {
                emit(Resource.Loading())
            try {

                emit(Resource.Success(data = loginRepository.setUpLogin(user)))
            }catch (e:Exception){
                Log.e("TAG", "LoginUseCase Exception: ----> ${e.message}", )
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}