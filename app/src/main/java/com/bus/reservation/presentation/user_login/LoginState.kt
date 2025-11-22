package com.bus.reservation.presentation.user_login

import com.bus.reservation.domain.model.LoginData

class LoginState(
    val isLoading: Boolean = false,
    val error: String? = "",
    val data: LoginData? = null
) {
}