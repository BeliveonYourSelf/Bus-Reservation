package com.bus.reservation.data.network

import com.bus.reservation.data.model.BookSeatResponse
import com.bus.reservation.data.model.OpenTicketResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TickerApiService {
    @POST("ticket/ticket")
    suspend fun registerTicket(@Body requestBody: RequestBody): BookSeatResponse

    @GET("ticket/open")
    suspend fun getAllOpenTicket(): OpenTicketResponse

    @GET("ticket/close")
    suspend fun getAllCloseTicket(): OpenTicketResponse
}