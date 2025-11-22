package com.bus.reservation.comman

object SharePrefranceUtills {


    const val APIKEY = "https://kube.artoon.in:32303/"
    const val SeatSuccess = "SeatSuccess"
    const val BusModel = "BusModel"
    const val BookModel = "BookModel"
    const val BusName = "BusName"
     const val INTRO = "INTRO"
     const val LOGIN = "LOGIN"
     const val AUTH_TOKEN = "AUTH_TOKEN"
     const val FULLNAME = "FULLNAME"
     const val USERNAME = "USERNAME"
     const val PHONE = "PHONE"
     const val EMAIL = "EMAIL"
     const val DATE = "DATE"
     const val TIME = "TIME"
     const val PROFILE = "PROFILE"
     const val NOTIFICATION = "NOTIFICATION"
     const val ISBOOK = "ISBOOK"
     const val SEATLIST = "SEATLIST"


    var isLogin: Boolean
        get() = sharePreranceManager.getBooleanValue(LOGIN, false)
        set(value) = sharePreranceManager.setBooleanValue(LOGIN, value)
    var isIntroComplete: Boolean
        get() = sharePreranceManager.getBooleanValue(INTRO, false)
        set(value) = sharePreranceManager.setBooleanValue(INTRO, value)



    var isBook: Boolean
        get() = sharePreranceManager.getBooleanValue(ISBOOK, false)
        set(value) = sharePreranceManager.setBooleanValue(ISBOOK, value)


    var isNotificationOn: Boolean
        get() = sharePreranceManager.getBooleanValue(NOTIFICATION, true)
        set(value) = sharePreranceManager.setBooleanValue(NOTIFICATION, value)

    var oAuthToken: String?
        get() = sharePreranceManager.getStringValue(AUTH_TOKEN, "")
        set(value) = sharePreranceManager.setStringValue(AUTH_TOKEN, value)

    var userName: String?
        get() = sharePreranceManager.getStringValue(USERNAME, "")
        set(value) {
            sharePreranceManager.setStringValue(USERNAME, value)
        }
    var fullName: String?
        get() = sharePreranceManager.getStringValue(FULLNAME, "")
        set(value) {
            sharePreranceManager.setStringValue(FULLNAME, value)
        }

    var phoneNUmber: String?
        get() = sharePreranceManager.getStringValue(PHONE, "")
        set(value) = sharePreranceManager.setStringValue(PHONE, value)

    var emailAddress: String?
        get() = sharePreranceManager.getStringValue(EMAIL, "")
        set(value) = sharePreranceManager.setStringValue(EMAIL, value)

    var profile: String?
        get() = sharePreranceManager.getStringValue(PROFILE, "")
        set(value) = sharePreranceManager.setStringValue(PROFILE, value)


    var seatList: String?
        get() = sharePreranceManager.getStringValue(SEATLIST, "")
        set(value) = sharePreranceManager.setStringValue(SEATLIST, value)

}