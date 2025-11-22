package com.bus.reservation.data.repository

import com.bus.reservation.data.model.ChangePasswordResponse
import com.bus.reservation.data.model.LoginResponse
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.data.model.UserResponse
import com.bus.reservation.data.model.UserTicketDetailsResponse
import com.bus.reservation.data.network.UserApiService
import com.bus.reservation.domain.model.LoginUser
import com.bus.reservation.domain.repository.UserRepository
import okhttp3.RequestBody

class UserRepoImpl(private val userApiService: UserApiService) : UserRepository {
    override suspend fun setUpLogin(user: LoginUser): LoginResponse {
        return userApiService.userLogin(user)
    }

    override suspend fun registerUser(requestBody: RequestBody): UserResponse {
        return userApiService.userRegister(requestBody)
    }

    override suspend fun getUserAllTickets(): UserAllTicketsResponse {
        return userApiService.getUserAllTickets()
    }

    override suspend fun getUserTicketDetails(requestBody: RequestBody): UserTicketDetailsResponse {
        return userApiService.getUserTicketDetails(requestBody)
    }

    override suspend fun changePassword(requestBody: RequestBody): ChangePasswordResponse {
        return userApiService.changePassword(requestBody)
    }
}