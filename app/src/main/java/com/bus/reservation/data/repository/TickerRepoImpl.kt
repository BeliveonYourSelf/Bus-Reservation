package com.bus.reservation.data.repository

import com.bus.reservation.data.model.BookSeatResponse
import com.bus.reservation.data.model.OpenTicketResponse
import com.bus.reservation.data.network.TickerApiService
import com.bus.reservation.domain.repository.TickerRepository
import okhttp3.RequestBody

class TickerRepoImpl(private val tickerApiService: TickerApiService) : TickerRepository {
    override suspend fun registerTicket(requestBody: RequestBody): BookSeatResponse {
        return tickerApiService.registerTicket(requestBody)
    }
    override suspend fun getAllOpenTicket(): OpenTicketResponse {
        return tickerApiService.getAllOpenTicket()
    }

    override suspend fun getAllCloseTicket(): OpenTicketResponse {
        return tickerApiService.getAllCloseTicket()
    }
}