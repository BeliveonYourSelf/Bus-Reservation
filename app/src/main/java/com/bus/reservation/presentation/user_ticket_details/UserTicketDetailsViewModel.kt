package com.bus.reservation.presentation.user_ticket_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.data.model.UserTicketDetailsResponse
import com.bus.reservation.domain.usecase.GetAllUserTicketUseCase
import com.bus.reservation.domain.usecase.UserTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserTicketDetailsViewModel @Inject constructor(private val getAllUserTicketUseCase: GetAllUserTicketUseCase, private val userTicketUseCase: UserTicketUseCase) :
    ViewModel() {

    private val _userAllTicketResponse = MutableStateFlow(UserAllTicketState())
    var userAllTicketResponse: StateFlow<UserAllTicketState> = _userAllTicketResponse

    private val _userTicketResponse = MutableStateFlow(UserAllTicketState())
    var userTicketResponse: StateFlow<UserAllTicketState> = _userTicketResponse

    fun getUserAllTicket() {
        viewModelScope.launch {
            getAllUserTicketUseCase.invoke()
                .collect { it: Resource<UserAllTicketsResponse> ->
                    when (it) {
                        is Resource.Error -> {
                            _userAllTicketResponse.emit(UserAllTicketState(message = it.message))
                        }

                        is Resource.Loading -> {
                            _userAllTicketResponse.emit(UserAllTicketState(isLoading = true))
                        }

                        is Resource.Success -> {
                            _userAllTicketResponse.emit(UserAllTicketState(data = it.data?.data))
                        }
                    }

                }
        }
    }

    fun getUserTicket(requestBody: RequestBody) {
        viewModelScope.launch {
            userTicketUseCase.invoke(requestBody)
                .collect { it: Resource<UserTicketDetailsResponse> ->
                    when (it) {
                        is Resource.Error -> {
                            _userTicketResponse.emit(UserAllTicketState(message = it.message))
                        }

                        is Resource.Loading -> {
                            _userTicketResponse.emit(UserAllTicketState(isLoading = true))
                        }

                        is Resource.Success -> {
                            _userTicketResponse.emit(UserAllTicketState(data = it.data?.data))
                        }
                    }

                }
        }
    }
}