package com.bus.reservation.presentation.user_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bus.reservation.comman.Resource
import com.bus.reservation.domain.model.LoginUser
import com.bus.reservation.domain.usecase.LoginUseCase
import com.bus.reservation.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginusercase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _loginResponse = MutableStateFlow(LoginState())
    var loginResponse = _loginResponse.asStateFlow()

    private val _userRegisterResponse = MutableStateFlow(UserRegisterState())
    var userRegisterResponse = _userRegisterResponse.asStateFlow()


    fun setupLogin(user: LoginUser) {
        viewModelScope.launch {
            loginusercase.invoke(user).collect {
                when (it) {
                    is Resource.Loading -> {
                        _loginResponse.value = LoginState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _loginResponse.value = LoginState(data = it.data!!.data)
                    }

                    is Resource.Error -> {
                        _loginResponse.value = LoginState(error = it.message)
                    }
                }
            }

        }
    }

    fun registerUser(requestBody: RequestBody) {
        viewModelScope.launch {
            registerUseCase.invoke(requestBody).collect {
                when (it) {
                    is Resource.Loading -> {
                        _userRegisterResponse.emit(UserRegisterState(isLoading = true))
                    }

                    is Resource.Success -> {
                        _userRegisterResponse.emit(UserRegisterState(data = it.data!!.data))
                    }

                    is Resource.Error -> {
                        _userRegisterResponse.emit(UserRegisterState(error = it.message))
                    }
                }
            }
        }
    }
}