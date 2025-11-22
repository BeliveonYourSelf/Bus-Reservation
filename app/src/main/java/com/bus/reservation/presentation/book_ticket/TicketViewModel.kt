package com.bus.reservation.presentation.book_ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.OpenTicketResponse
import com.bus.reservation.domain.usecase.CloseTicketUseCase
import com.bus.reservation.domain.usecase.OpenTicketUseCase
import com.bus.reservation.domain.usecase.RegisterTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketUseCase: OpenTicketUseCase,
    private val closeTicketUseCase: CloseTicketUseCase,
    private val registerTickerUseCase: RegisterTickerUseCase,
) :
    ViewModel() {

    private val _bookTicketResponse = MutableStateFlow(BookState())
    var bookTicketResponse: StateFlow<BookState> = _bookTicketResponse

    private val _openTicketResponse = MutableStateFlow(TicketState())
    var openTicketResponse= _openTicketResponse.asStateFlow()

    private val _closeTicketResponse = MutableStateFlow(TicketState())
    val closeTicketResponse = _closeTicketResponse.asStateFlow()


    fun bookTicket(requestBody: RequestBody) {
        viewModelScope.launch {
            registerTickerUseCase.invoke(requestBody).collect({ responseBooking ->

                when (responseBooking) {
                    is Resource.Loading -> {
                        _bookTicketResponse.emit(BookState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _bookTicketResponse.emit(BookState(message = responseBooking.message))
                    }

                    is Resource.Success -> {
                        _bookTicketResponse.emit(BookState(data = responseBooking.data?.data))
                    }
                }

            })
        }
    }

    fun getAllOpenTicket() {
        viewModelScope.launch {
            ticketUseCase.invoke().collect { response: Resource<OpenTicketResponse> ->
                when (response) {
                    is Resource.Loading -> {
                        _openTicketResponse.emit(TicketState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _openTicketResponse.emit(TicketState(message = response.message))
                    }

                    is Resource.Success -> {
                        _openTicketResponse.emit(TicketState(data = response.data?.data))
                    }
                }

            }
        }
    }

    fun getAllCloseTickets() {
        viewModelScope.launch {
            closeTicketUseCase.invoke().collect { closeResponse: Resource<OpenTicketResponse> ->
                when (closeResponse) {
                    is Resource.Error -> _closeTicketResponse.emit(TicketState(message = closeResponse.message))
                    is Resource.Loading -> _closeTicketResponse.emit(TicketState(isLoading = true))
                    is Resource.Success -> _closeTicketResponse.emit(TicketState(data = closeResponse.data?.data))
                }

            }
        }
    }
}