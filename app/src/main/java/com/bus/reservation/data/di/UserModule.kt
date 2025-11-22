package com.bus.reservation.data.di

import com.bus.reservation.data.network.UserApiService
import com.bus.reservation.data.repository.UserRepoImpl
import com.bus.reservation.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun providesLogin(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    fun providesLoginImpl(userApiService: UserApiService): UserRepository {
        return UserRepoImpl(userApiService)
    }
}