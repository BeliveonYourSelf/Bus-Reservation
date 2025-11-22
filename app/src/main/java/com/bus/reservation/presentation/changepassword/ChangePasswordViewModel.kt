package com.bus.reservation.presentation.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.ChangePasswordResponse
import com.bus.reservation.domain.usecase.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel  @Inject constructor(val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {

    private val _changePasswordResponse = MutableStateFlow(ChangePassState())
    var  changPasswordResponse =_changePasswordResponse.asStateFlow()


    fun changePassword(requestBody: RequestBody){
        viewModelScope.launch {
        changePasswordUseCase.invoke(requestBody).collect({ it: Resource<ChangePasswordResponse> ->

            when(it){
                is Resource.Error -> {
                    _changePasswordResponse.emit(ChangePassState(data = it.message))
                }
                is Resource.Loading -> {
                    _changePasswordResponse.emit(ChangePassState(isLoading = true))
                }
                is Resource.Success -> {
                    _changePasswordResponse.emit(ChangePassState(success = it.data!!.success))
                }
            }
        })
        }

    }

}