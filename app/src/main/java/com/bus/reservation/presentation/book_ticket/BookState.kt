package com.bus.reservation.presentation.book_ticket

import com.bus.reservation.data.model.BookSearData

data class BookState(
    val isLoading: Boolean = false,
    val data: BookSearData? = null,
    val message: String? = "",
) {
}