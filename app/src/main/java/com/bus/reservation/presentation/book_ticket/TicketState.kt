package com.bus.reservation.presentation.book_ticket

import com.bus.reservation.domain.model.OpenTicketData

data class TicketState(
    val isLoading: Boolean = false,
    val data: List<OpenTicketData>? = null,
    val message: String? = "",
) {
}