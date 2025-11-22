package com.bus.reservation.presentation.changepassword

data class ChangePassState(
    val isLoading: Boolean = false,
    val data: String? = "",
    val success: Boolean = false,
)
