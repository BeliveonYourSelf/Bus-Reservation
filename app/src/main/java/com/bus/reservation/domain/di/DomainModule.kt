package com.bus.reservation.domain.di

import androidx.work.WorkManager
import com.bus.reservation.domain.repository.BusRepository
import com.bus.reservation.domain.repository.TickerRepository
import com.bus.reservation.domain.repository.UserRepository
import com.bus.reservation.domain.usecase.BusListUserCase
import com.bus.reservation.domain.usecase.BusSearchUseCase
import com.bus.reservation.domain.usecase.ChangePasswordUseCase
import com.bus.reservation.domain.usecase.CloseTicketUseCase
import com.bus.reservation.domain.usecase.GetAllUserTicketUseCase
import com.bus.reservation.domain.usecase.LoginUseCase
import com.bus.reservation.domain.usecase.OpenTicketUseCase
import com.bus.reservation.domain.usecase.RegisterTickerUseCase
import com.bus.reservation.domain.usecase.RegisterUseCase
import com.bus.reservation.domain.usecase.UserTicketUseCase
import com.bus.reservation.worker.PostNotificationWorkManger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideUserLoginUsecase(loginRepository: UserRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Provides
    fun providesUserRegisterUseCase(loginRepository: UserRepository): RegisterUseCase {
        return RegisterUseCase(loginRepository)
    }

    @Provides
    fun providesBusListUseCase(busRepository: BusRepository): BusListUserCase {
        return BusListUserCase(busRepository = busRepository)
    }

    @Provides
    fun providesBusSerachList(busRepository: BusRepository): BusSearchUseCase {
        return BusSearchUseCase(busRepository = busRepository)
    }

    @Provides
    fun providesOpenTicketUseCase(tickerRepository: TickerRepository): OpenTicketUseCase {
        return OpenTicketUseCase(tickerRepository)
    }


    @Provides
    fun provideBookTickets(tickerRepository: TickerRepository): RegisterTickerUseCase {
        return RegisterTickerUseCase(tickerRepository)
    }

    @Provides
    fun provideCloseTicketUseCase(tickerRepository: TickerRepository): CloseTicketUseCase {
        return CloseTicketUseCase(tickerRepository)
    }

    @Provides
    fun provideSendNotificationWorker(workManager: WorkManager): PostNotificationWorkManger {
        return PostNotificationWorkManger(workManager)
    }

    @Provides
    fun provideUserAllTickets(repository: UserRepository): GetAllUserTicketUseCase {
        return GetAllUserTicketUseCase(userRepository = repository)
    }

    @Provides
    fun provideUserTickets(repository: UserRepository): UserTicketUseCase {
        return UserTicketUseCase(userRepository = repository)
    }

    @Provides
    fun proivdeChangePasswordUseCase(userRepository: UserRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(userRepository)
    }


}