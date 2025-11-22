package com.bus.reservation.presentation.user_login

import com.bus.reservation.domain.model.UserData

data class UserRegisterState(
    val isLoading: Boolean = false,
    val error: String? = "",
    val data: UserData? = null
)
