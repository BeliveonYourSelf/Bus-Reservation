package com.bus.reservation.domain.repository

import com.bus.reservation.data.model.BookSeatResponse
import com.bus.reservation.data.model.OpenTicketResponse
import okhttp3.RequestBody

interface TickerRepository {
    suspend fun registerTicket(requestBody: RequestBody): BookSeatResponse
    suspend fun getAllOpenTicket(): OpenTicketResponse
    suspend fun getAllCloseTicket(): OpenTicketResponse
}