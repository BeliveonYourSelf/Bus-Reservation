package com.bus.reservation.domain.repository

import com.bus.reservation.data.model.ChangePasswordResponse
import com.bus.reservation.data.model.LoginResponse
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.data.model.UserResponse
import com.bus.reservation.data.model.UserTicketDetailsResponse
import com.bus.reservation.domain.model.LoginUser
import okhttp3.RequestBody

interface UserRepository {
    suspend fun setUpLogin(user: LoginUser): LoginResponse
    suspend fun registerUser(requestBody: RequestBody): UserResponse
    suspend fun getUserAllTickets(): UserAllTicketsResponse
    suspend fun getUserTicketDetails(requestBody: RequestBody): UserTicketDetailsResponse
    suspend fun changePassword(requestBody: RequestBody) : ChangePasswordResponse
}