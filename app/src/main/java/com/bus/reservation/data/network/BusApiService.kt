package com.bus.reservation.data.network

import com.bus.reservation.data.model.BusListResponse
import com.bus.reservation.data.model.BusSearchResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface BusApiService {

    @POST("bus/add")
    suspend fun busAdd(name: String): String

    @POST("bus/buslist")
    suspend fun getAllBusList(
        @Query("per_page") per_page: String,
        @Query("page_no") page_no: String,
    ): BusListResponse

    @POST("/bus/buslist")
    suspend fun getBusList(@Body requestBody: RequestBody): BusListResponse

    @POST("/bus/searchbus")
    suspend fun searchBus(@Body requestBody: RequestBody): BusSearchResponse
}