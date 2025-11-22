package com.bus.reservation.data.network

import com.bus.reservation.data.model.ChangePasswordResponse
import com.bus.reservation.data.model.LoginResponse
import com.bus.reservation.data.model.UserAllTicketsResponse
import com.bus.reservation.data.model.UserResponse
import com.bus.reservation.data.model.UserTicketDetailsResponse
import com.bus.reservation.domain.model.LoginUser
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {

    @POST("user/login")
    suspend fun userLogin(@Body user: LoginUser): LoginResponse

    @POST("user/register")
    suspend fun userRegister(@Body userDetails: RequestBody): UserResponse

    @POST("user/ticket")
    suspend fun getUserTicketDetails(@Body userDetails: RequestBody): UserTicketDetailsResponse

    @GET("user/alltickets")
    suspend fun getUserAllTickets(): UserAllTicketsResponse

    @POST("user/changePassword")
    suspend fun changePassword(@Body requestBody: RequestBody) : ChangePasswordResponse
}