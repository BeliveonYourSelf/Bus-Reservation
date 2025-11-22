package com.bus.reservation.presentation.user_ticket_details

import com.bus.reservation.domain.model.UserTicketDetailsData

data class UserAllTicketState(
    val isLoading: Boolean = false,
    val data: List<UserTicketDetailsData>? = null,
    val message: String? = "",
)
