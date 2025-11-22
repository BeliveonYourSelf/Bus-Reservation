package com.bus.reservation.comman

import com.bus.reservation.data.model.LoginResponse
import com.bus.reservation.domain.model.LoginData


fun LoginResponse.toDomain(): LoginData {
    return LoginData(
        birthdate = this.data.birthdate,
        city = this.data.city,
        country = this.data.country,
        email = this.data.email,
        firstName = this.data.firstName,
        gender = this.data.gender,
        id = this.data.id,
        lastName = this.data.lastName,
        phone = this.data.phone,
        state = this.data.state,
        token = this.data.token
    )
}

interface Mapper<F, T> {
    fun map(from: F): T
}

fun <F, T> Mapper<F, T>.mapAll(list: List<F>) {
    list.map {
        map(it)
    }
}