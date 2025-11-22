package com.bus.reservation.domain.usecase

import android.util.Log
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.UserResponse
import com.bus.reservation.domain.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody

class RegisterUseCase(private val loginRepository: UserRepository) {
     operator fun invoke(requestBody: RequestBody): Flow<Resource<UserResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
//                Log.e("TAG", "RegisterUseCase Success: ----> ${GsonBuilder().setPrettyPrinting().create().toJson(loginRepository.registerUser(requestBody))}", )
                emit(Resource.Success(data = loginRepository.registerUser(requestBody)))
            } catch (e: Exception) {
                e.message?.toResponseBody("plain/text".toMediaTypeOrNull())
                Log.e("RegisterUseCase", "invoke message: ------>${e.message}", )
                Log.e("RegisterUseCase", "invoke: state  ------>${e.printStackTrace()}", )
                Log.e("RegisterUseCase", "invoke: case  ------>${e.cause}", )
                if(e.message == "HTTP 409 Conflict")
                {
                    emit(Resource.Error("Email Already Exist. Please Login"))
                }else{
                emit(Resource.Error(e.message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}