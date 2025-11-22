package com.bus.reservation.data.di

import com.bus.reservation.data.network.TickerApiService
import com.bus.reservation.data.repository.TickerRepoImpl
import com.bus.reservation.domain.repository.TickerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object TicketModule {

    @Provides
    fun provideTicketApiService(retrofit: Retrofit): TickerApiService {
        return retrofit.create(TickerApiService::class.java)
    }

    @Provides
    fun provideTicketRepositroy(ticketApiService: TickerApiService): TickerRepository {
        return TickerRepoImpl(tickerApiService = ticketApiService)
    }
}