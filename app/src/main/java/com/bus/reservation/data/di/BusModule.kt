package com.bus.reservation.data.di

import com.bus.reservation.data.network.BusApiService
import com.bus.reservation.data.repository.BusRepoImpl
import com.bus.reservation.domain.repository.BusRepository
import com.bus.reservation.paging.BuslistRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object BusModule {


    @Provides
    fun proivdeBusService(retrofit: Retrofit): BusApiService {
        return retrofit.create(BusApiService::class.java)
    }

    @Provides
    fun providesBusImpl(busApiService: BusApiService): BusRepository {
        return BusRepoImpl(busApiService)
    }

    @Provides
    fun provideBusListRepo(busApiService: BusApiService): BuslistRepo {
        return BuslistRepo(busApiService)
    }


}